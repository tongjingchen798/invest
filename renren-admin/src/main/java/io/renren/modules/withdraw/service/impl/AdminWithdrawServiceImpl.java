package io.renren.modules.withdraw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.math.BigDecimal;

/**
 * 管理员提现服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@Service("adminWithdrawService")
public class AdminWithdrawServiceImpl implements AdminWithdrawService {

    @Autowired
    private WithdrawOrderDao withdrawOrderDao;

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
            // 这里可以添加将资金退回给用户的逻辑
            // 由于前端提现时已经扣除了用户余额，驳回时需要退回
            // 具体实现需要根据业务需求来决定
            
            log.info("提现驳回，需要将资金 {} 退回给用户 {}", withdrawAmount, withdrawOrder.getUserId());
            
            // TODO: 实现资金退回逻辑
            // 1. 更新用户余额
            // 2. 记录账变明细
            // 3. 发送通知等
            
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
}
