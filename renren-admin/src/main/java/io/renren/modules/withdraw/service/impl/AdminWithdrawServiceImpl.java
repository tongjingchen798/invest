package io.renren.modules.withdraw.service.impl;

import io.renren.modules.finance.dao.UserBalanceDetailDao;
import io.renren.modules.finance.entity.UserBalanceDetailEntity;
import io.renren.modules.member.dao.MemberDao;
import io.renren.modules.member.entity.MemberEntity;
import io.renren.modules.withdraw.dao.WithdrawOrderDao;
import io.renren.modules.withdraw.dto.WithdrawAuditDTO;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;
import io.renren.modules.withdraw.service.AdminWithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 管理员提现服务实现类
 *
 * @author nico
 */
@Slf4j
@Service("adminWithdrawService")
public class AdminWithdrawServiceImpl implements AdminWithdrawService {

    @Autowired
    private WithdrawOrderDao withdrawOrderDao;
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditWithdraw(WithdrawAuditDTO auditDTO) {
        try {
            // 查询提现订单
            WithdrawOrderEntity withdrawOrder = withdrawOrderDao.selectById(auditDTO.getId().toString());
            if (withdrawOrder == null) {
                throw new RuntimeException("提现订单不存在");
            }

            // 检查订单状态是否允许审核
            if (!canAudit(withdrawOrder.getState())) {
                throw new RuntimeException("当前订单状态不允许审核操作");
            }

            // 记录原始状态，用于后续处理
            Integer originalState = withdrawOrder.getState();
            Long withdrawAmount = withdrawOrder.getInputamount();

            // 更新订单状态
            withdrawOrder.setState(auditDTO.getState());
            
            // 设置审核时间
            withdrawOrder.setStateTime(new Date());
            
            // 设置审核备注
            if (StringUtils.hasText(auditDTO.getRemark())) {
                withdrawOrder.setRemark(auditDTO.getRemark());
            }
            
            // 设置操作人ID
            withdrawOrder.setOperCode(auditDTO.getSysUpdateUserId().toString());

            // 根据审核状态设置相应的消息
            String msg = getStatusMessage(auditDTO.getState());
            if (StringUtils.hasText(msg)) {
                withdrawOrder.setMsg(msg);
            }

            // 更新订单
            int updateResult = withdrawOrderDao.updateById(withdrawOrder);
            if (updateResult <= 0) {
                throw new RuntimeException("更新提现订单失败");
            }

            // 处理审核后的业务逻辑
            handlePostAuditBusinessLogic(withdrawOrder, originalState, auditDTO.getState(), withdrawAmount);

            log.info("提现审核成功，订单ID: {}, 状态: {}, 操作人: {}", 
                    auditDTO.getId(), auditDTO.getState(), auditDTO.getSysUpdateUserId());

        } catch (Exception e) {
            log.error("提现审核失败，订单ID: {}, 错误信息: {}", auditDTO.getId(), e.getMessage(), e);
            throw new RuntimeException("提现审核失败: " + e.getMessage());
        }
    }

    /**
     * 处理审核后的业务逻辑
     */
    private void handlePostAuditBusinessLogic(WithdrawOrderEntity withdrawOrder, Integer originalState, 
                                           Integer newState, Long withdrawAmount) {
        try {
            // 如果是从待审核状态变为其他状态，需要处理余额相关逻辑
            if (originalState == 0) {
                switch (newState) {
                    case 1: // 审核通过
                        log.info("提现审核通过，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                        // 审核通过不需要额外处理，资金已经在前端提现时扣除
                        WithdrawOrderEntity order = withdrawOrderDao.selectByOrderno(withdrawOrder.getOrderno());
                        MemberEntity user = memberDao.selectById(Long.valueOf(order.getUserId()));

                        // 从冻结余额中真正扣减
                        user.setFreezeBalance(user.getFreezeBalance() - order.getAmount());
                        memberDao.updateById(user);

                        // 更新订单状态
                        order.setState(1); // 审核通过
                        withdrawOrderDao.updateById(order);

                        // 记录账变明细（提现成功）
                        recordBalanceDetail(order, user,order.getAmount(),"佣金提现"); // 2-余额提现流水

                        break;
                        
                    case 2: // 手动转款
                        log.info("提现手动转款，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                        // 手动转款不需要额外处理
                        break;
                        
                    case 3: // 审核驳回
                        log.info("提现审核驳回，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                        // 审核驳回需要将资金退回给用户
                        handleWithdrawRejection(withdrawOrder, withdrawAmount);
                        break;
                        
                    case 5: // 再次提交
                        log.info("提现再次提交，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                        // 再次提交不需要额外处理
                        break;
                        
                    default:
                        log.warn("未知的审核状态: {}, 订单ID: {}", newState, withdrawOrder.getId());
                        break;
                }
            }
        } catch (Exception e) {
            log.error("处理审核后业务逻辑失败，订单ID: {}, 错误信息: {}", withdrawOrder.getId(), e.getMessage(), e);
            throw new RuntimeException("处理审核后业务逻辑失败: " + e.getMessage());
        }
    }

    /**
     * 处理提现驳回逻辑
     */
    private void handleWithdrawRejection(WithdrawOrderEntity withdrawOrder, Long withdrawAmount) {
        try {
            log.info("提现驳回，需要将资金 {} 退回给用户 {}", withdrawAmount, withdrawOrder.getUserId());

            WithdrawOrderEntity order = withdrawOrderDao.selectByOrderno(withdrawOrder.getOrderno());
            MemberEntity user = memberDao.selectById(Long.valueOf(order.getUserId()));

            // 解冻资金，返还到可用余额
            user.setFreezeBalance(user.getFreezeBalance() - order.getAmount());
            //提现类型 1余额提现 2佣金提现
            if(withdrawOrder.getWithdrawType()==1){
                user.setCashwithdrawable(user.getCashwithdrawable() + order.getAmount());
            }else {
                user.setCommissionBalance(user.getCommissionBalance() + order.getAmount());
            }
            memberDao.updateById(user);

            // 更新订单状态
            order.setState(2); // 审核拒绝
            order.setRemark("审核拒绝");
            withdrawOrderDao.updateById(order);

            // 记录账变明细（解冻记录）
//            recordBalanceDetail(Long.valueOf(order.getUserId()), order.getAmount(), "佣金提现解冻", 6); // 6-解冻金额

        } catch (Exception e) {
            log.error("处理提现驳回失败，订单ID: {}, 错误信息: {}", withdrawOrder.getId(), e.getMessage(), e);
            throw new RuntimeException("处理提现驳回失败: " + e.getMessage());
        }
    }

    /**
     * 检查订单状态是否允许审核
     */
    private boolean canAudit(Integer currentState) {
        // 只有待审核状态(0)的订单才能进行审核
        return currentState != null && currentState == 0;
    }

    /**
     * 根据审核状态获取相应的消息
     */
    private String getStatusMessage(Integer state) {
        switch (state) {
            case 1:
                return "审核通过";
            case 2:
                return "手动转款";
            case 3:
                return "审核驳回";
            case 5:
                return "再次提交";
            default:
                return null;
        }
    }

    /**
     * 记录余额明细
     */
    private void recordBalanceDetail(WithdrawOrderEntity withdrawOrder, MemberEntity user, Long amountInCents,String remark) {
        try {
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(Long.valueOf(withdrawOrder.getUserId()));
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(user.getAgent());
            balanceDetail.setAgentName(user.getAgentName());
            //2 余额提现 33佣金提现
            if(withdrawOrder.getWithdrawType()==1){
                balanceDetail.setBusiType(2);
            }else {
                balanceDetail.setBusiType(33);
            }

            balanceDetail.setChannel("WePay");
            balanceDetail.setOriginalAmount(user.getAssets() != null ? user.getAssets() - amountInCents : 0L);
            balanceDetail.setTransactionAmount(user.getAssets() != null ? user.getAssets() : amountInCents);
            balanceDetail.setUseAmount(amountInCents);
            balanceDetail.setRemarks(remark);
            balanceDetail.setSalesmanName(user.getSalesmanName());
            balanceDetail.setSalesmanId(user.getSalesmanid());
            balanceDetail.setStatus(1); // 1-正常
            balanceDetail.setStreamId(withdrawOrder.getThreeorderNo());
            balanceDetail.setCreateDate(new Date());
            balanceDetail.setUpdateDate(new Date());

            userBalanceDetailDao.insert(balanceDetail);

        } catch (Exception e) {
            log.error("记录余额明细失败 - 用户ID: {}, 金额: {} 分", withdrawOrder.getUserId(), amountInCents, e);
            throw e;
        }
    }
}
