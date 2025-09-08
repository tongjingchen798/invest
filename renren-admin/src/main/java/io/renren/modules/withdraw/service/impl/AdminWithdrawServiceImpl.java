package io.renren.modules.withdraw.service.impl;

import io.renren.common.exception.RenException;
import io.renren.modules.finance.dao.UserBalanceDetailDao;
import io.renren.modules.finance.entity.UserBalanceDetailEntity;
import io.renren.modules.member.dao.MemberDao;
import io.renren.modules.member.entity.MemberEntity;
import io.renren.modules.withdraw.dao.WithdrawOrderDao;
import io.renren.modules.withdraw.dto.WithdrawAuditDTO;
import io.renren.modules.withdraw.dto.PayAgentResponse;
import io.renren.modules.withdraw.entity.WithdrawOrderEntity;
import io.renren.modules.withdraw.service.AdminWithdrawService;
import io.renren.modules.withdraw.service.PayAgentFactory;
import io.renren.modules.paymerchant.entity.PayMerchantEntity;
import io.renren.modules.paymerchant.dao.PayMerchantDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 管理员提现服务实现类
 *
 * @author nico
 */
@Slf4j
@Service("adminWithdrawService")
public class AdminWithdrawServiceImpl implements AdminWithdrawService {
    
    /**
     * 提现订单状态常量
     */
    private static final int STATE_PENDING = 0;      // 待审核
    private static final int STATE_APPROVED = 1;     // 审核通过
    private static final int STATE_WITHDRAWN = 2;    // 已提现
    private static final int STATE_REJECTED = 3;     // 驳回
    private static final int STATE_FAILED = 4;       // 提现失败
    private static final int STATE_INVALID = 5;      // 无效订单

    @Autowired
    private WithdrawOrderDao withdrawOrderDao;
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private UserBalanceDetailDao userBalanceDetailDao;

    @Autowired
    private PayAgentFactory payAgentFactory;

    @Autowired
    private PayMerchantDao payMerchantDao;

    /**
     * 订单号生成器
     */
    private static final AtomicLong orderNoGenerator = new AtomicLong(System.currentTimeMillis());

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditWithdraw(WithdrawAuditDTO auditDTO) {
        try {
            // 查询提现订单
            WithdrawOrderEntity withdrawOrder = withdrawOrderDao.selectById(auditDTO.getId().toString());
            if (withdrawOrder == null) {
                throw new RenException("提现订单不存在");
            }

//            // 检查订单状态是否允许审核
//            if (!canAudit(withdrawOrder.getState())) {
//                if(auditDTO.getState()!=5) {
//                    throw new RenException("当前订单状态不允许审核操作");
//                }
//            }


            // 记录原始状态，用于后续处理
            Integer originalState = withdrawOrder.getState();
            //提现金额
            Long withdrawAmount = withdrawOrder.getAmount();

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
                throw new RenException("更新提现订单失败");
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
        // 如果是从待审核状态变为其他状态，需要处理余额相关逻辑
//        if (originalState == STATE_PENDING) {
            switch (newState) {
                case STATE_APPROVED:
                    // 审核通过
                    log.info("提现审核通过，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                    handleWithdrawApproval(withdrawOrder, withdrawAmount);
                    break;

                case STATE_WITHDRAWN:
                    // 已提现（手动转款）
                    log.info("提现手动转款，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                    // 手动转款不需要额外处理
                    break;

                case STATE_REJECTED:
                    // 驳回
                    log.info("提现审核驳回，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                    // 审核驳回需要将资金退回给用户
                    handleWithdrawRejection(withdrawOrder, withdrawAmount);
                    break;

                 case STATE_FAILED:
                     // 提现失败
                     log.info("提现失败，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                     // 提现失败需要将资金退回给用户
                     handleWithdrawRejection(withdrawOrder, withdrawAmount);
                     break;

                case STATE_INVALID:
                    // 重新发起提现
                    log.info("无效订单，订单ID: {}, 金额: {}", withdrawOrder.getId(), withdrawAmount);
                    // 无效订单需要将资金退回给用户，并创建新订单
                    handleWithdrawInvalid(withdrawOrder, withdrawAmount);
                    break;

                default:
                    log.warn("未知的审核状态: {}, 订单ID: {}", newState, withdrawOrder.getId());
                    break;
            }
//        }

    }

    /**
     * 处理提现审核通过
     */
    private void handleWithdrawApproval(WithdrawOrderEntity withdrawOrder, Long withdrawAmount) {
        log.info("开始处理提现审核通过 - 订单号: {}, 金额: {}", withdrawOrder.getOrderno(), withdrawAmount);

        WithdrawOrderEntity order = withdrawOrderDao.selectByOrderno(withdrawOrder.getOrderno());
        if (order == null) {
            throw new RenException("订单不存在,请刷新页面");
        }
        MemberEntity user = memberDao.selectById(Long.valueOf(order.getUserId()));
        if (user == null) {
            throw new RenException("用户已停用");
        }
        order.setState(STATE_APPROVED); // 审核通过
        order.setStateTime(new Date());
        withdrawOrderDao.updateById(order);
        //调用代付接口
        PayMerchantEntity payMerchant = payMerchantDao.selectById(withdrawOrder.getMerchantid());
        if (payMerchant == null) {
            throw new RenException("查询支付商户信息失败 - 商户ID: " + withdrawOrder.getMerchantid());
        }

        // 使用代付工厂创建代付订单
        PayAgentResponse payoutResponse = payAgentFactory.createPayoutOrder(withdrawOrder, payMerchant);

        if (payoutResponse.getSuccess()) {
            log.info("代付接口调用成功 - 订单号: {}, 渠道: {}, 第三方订单号: {}",
                    withdrawOrder.getOrderno(), payoutResponse.getChannel(), payoutResponse.getThirdOrderNo());

            // 更新订单的第三方订单号
            withdrawOrder.setThreeorderNo(payoutResponse.getThirdOrderNo());
            withdrawOrder.setRemark(withdrawOrder.getRemark() + " |【" + payoutResponse.getChannel() + "】代付已提交");
            withdrawOrderDao.updateById(withdrawOrder);

        } else {
            // 代付失败，需要回退提现金额
            log.error("代付接口调用失败 - 订单号: {}, 渠道: {}, 错误信息: {}", 
                     withdrawOrder.getOrderno(), payoutResponse.getChannel(), payoutResponse.getMessage());
            
            // 更新订单状态为失败
            order.setState(STATE_FAILED); // 提现失败
            order.setStateTime(new Date());
            order.setRemark("【" + payoutResponse.getChannel() + "】代付失败: " + payoutResponse.getMessage());
            withdrawOrderDao.updateById(order);
            
            // 回退提现金额：解冻资金，返还到可用余额（一条SQL完成）
            try {
                int result = memberDao.updateBalanceOnWithdrawFailure(
                    Long.valueOf(order.getUserId()), 
                    order.getAmount(), 
                    order.getWithdrawType()
                );
                
                if (result > 0) {
                    String withdrawTypeName = order.getWithdrawType() == 1 ? "余额提现" : "佣金提现";
                    log.info("代付失败，已回退{}金额 - 用户ID: {}, 金额: {}", 
                            withdrawTypeName, order.getUserId(), order.getAmount());
                } else {
                    log.warn("代付失败回退金额失败 - 用户ID: {}, 金额: {}, 影响行数: {}", 
                            order.getUserId(), order.getAmount(), result);
                }
                
                // 记录账变明细（代付失败回退）
                recordBalanceDetail(order, user, order.getAmount(), "代付失败回退");
                
            } catch (Exception e) {
                log.error("代付失败回退金额异常 - 订单号: {}, 用户ID: {}, 金额: {}", 
                         order.getOrderno(), order.getUserId(), order.getAmount(), e);
                // 回退失败也要记录，但不影响主流程
                order.setRemark(order.getRemark() + " | 回退金额失败: " + e.getMessage());
                withdrawOrderDao.updateById(order);
            }
        }
    }

    /**
     * 处理无效订单逻辑
     */
    private void handleWithdrawInvalid(WithdrawOrderEntity withdrawOrder, Long withdrawAmount) {
        try {
            log.info("处理无效订单，需要将资金 {} 退回给用户 {} 并创建新订单", withdrawAmount, withdrawOrder.getUserId());

            WithdrawOrderEntity order = withdrawOrderDao.selectByOrderno(withdrawOrder.getOrderno());
            MemberEntity user = memberDao.selectById(Long.valueOf(order.getUserId()));

//            // 解冻资金，返还到可用余额（一条SQL完成）
//            int result = memberDao.updateBalanceOnWithdrawFailure(
//                Long.valueOf(order.getUserId()),
//                order.getAmount(),
//                order.getWithdrawType()
//            );
//
//            if (result > 0) {
//                String withdrawTypeName = order.getWithdrawType() == 1 ? "余额提现" : "佣金提现";
//                log.info("无效订单，已回退{}金额 - 用户ID: {}, 金额: {}",
//                        withdrawTypeName, order.getUserId(), order.getAmount());
//            } else {
//                log.warn("无效订单回退金额失败 - 用户ID: {}, 金额: {}, 影响行数: {}",
//                        order.getUserId(), order.getAmount(), result);
//            }

            // 更新订单状态为无效订单
            order.setState(STATE_INVALID); // 无效订单
            order.setRemark("将当前这笔单改成无效，然后新增一笔");
            order.setStateTime(new Date());
            withdrawOrderDao.updateById(order);

            // 创建新的提现申请订单
            createNewWithdrawOrder(order, user);

            //调用代付接口
            PayMerchantEntity payMerchant = payMerchantDao.selectById(withdrawOrder.getMerchantid());
            if (payMerchant == null) {
                throw new RenException("查询支付商户信息失败 - 商户ID: " + withdrawOrder.getMerchantid());
            }
            // 使用代付工厂创建代付订单
            PayAgentResponse payoutResponse = payAgentFactory.createPayoutOrder(withdrawOrder, payMerchant);

            if (payoutResponse.getSuccess()) {
                log.info("代付接口调用成功 - 订单号: {}, 渠道: {}, 第三方订单号: {}",
                        withdrawOrder.getOrderno(), payoutResponse.getChannel(), payoutResponse.getThirdOrderNo());

                // 更新订单的第三方订单号
                withdrawOrder.setThreeorderNo(payoutResponse.getThirdOrderNo());
                withdrawOrder.setRemark(withdrawOrder.getRemark() + " |【" + payoutResponse.getChannel() + "】代付已提交");
                withdrawOrderDao.updateById(withdrawOrder);

            } else {
                // 代付失败，需要回退提现金额
                log.error("代付接口调用失败 - 订单号: {}, 渠道: {}, 错误信息: {}",
                        withdrawOrder.getOrderno(), payoutResponse.getChannel(), payoutResponse.getMessage());

                // 更新订单状态为失败
                order.setState(STATE_FAILED); // 提现失败
                order.setStateTime(new Date());
                order.setRemark("【" + payoutResponse.getChannel() + "】代付失败: " + payoutResponse.getMessage());
                withdrawOrderDao.updateById(order);

                // 回退提现金额：解冻资金，返还到可用余额（一条SQL完成）
                try {
                    int result = memberDao.updateBalanceOnWithdrawFailure(
                            Long.valueOf(order.getUserId()),
                            order.getAmount(),
                            order.getWithdrawType()
                    );

                    if (result > 0) {
                        String withdrawTypeName = order.getWithdrawType() == 1 ? "余额提现" : "佣金提现";
                        log.info("代付失败，已回退{}金额 - 用户ID: {}, 金额: {}",
                                withdrawTypeName, order.getUserId(), order.getAmount());
                    } else {
                        log.warn("代付失败回退金额失败 - 用户ID: {}, 金额: {}, 影响行数: {}",
                                order.getUserId(), order.getAmount(), result);
                    }

                    // 记录账变明细（代付失败回退）
                    recordBalanceDetail(order, user, order.getAmount(), "代付失败回退");

                } catch (Exception e) {
                    log.error("代付失败回退金额异常 - 订单号: {}, 用户ID: {}, 金额: {}",
                            order.getOrderno(), order.getUserId(), order.getAmount(), e);
                    // 回退失败也要记录，但不影响主流程
                    order.setRemark(order.getRemark() + " | 回退金额失败: " + e.getMessage());
                    withdrawOrderDao.updateById(order);
                }
            }

        } catch (Exception e) {
            log.error("处理无效订单失败，订单ID: {}, 错误信息: {}", withdrawOrder.getId(), e.getMessage(), e);
            throw new RuntimeException("处理无效订单失败: " + e.getMessage());
        }
    }

    /**
     * 创建新的提现申请订单
     */
    private void createNewWithdrawOrder(WithdrawOrderEntity originalOrder, MemberEntity user) {
        try {
            // 检查是否有正在处理中的提现订单
            Long pendingCount = withdrawOrderDao.selectPendingWithdrawCountByUserId(Long.valueOf(originalOrder.getUserId()));
            if (pendingCount.intValue() > 0) {
                log.warn("用户已有正在处理中的提现订单，无法创建新订单 - 用户ID: {}, 正在处理订单数: {}",
                        originalOrder.getUserId(), pendingCount);
                throw new RenException("用户已有正在处理中的提现订单，无法创建新订单");
            }

            // 生成新的订单号
            String newOrderNo = generateOrderNo();
            
            // 创建新的提现订单
            WithdrawOrderEntity newOrder = new WithdrawOrderEntity();
            newOrder.setId(String.valueOf(System.currentTimeMillis()));
            newOrder.setUserId(originalOrder.getUserId());
            newOrder.setMobile(originalOrder.getMobile());
            newOrder.setUsername(originalOrder.getUsername());
            newOrder.setAmount(originalOrder.getAmount());
            newOrder.setInputamount(originalOrder.getInputamount());
            newOrder.setHandFee(originalOrder.getHandFee());
            newOrder.setRealAmount(originalOrder.getRealAmount());
            newOrder.setChannel(originalOrder.getChannel());
            newOrder.setPayNo(originalOrder.getPayNo());
            newOrder.setPayName(originalOrder.getPayName());
            newOrder.setBlankCode(originalOrder.getBlankCode());
            newOrder.setBlankName(originalOrder.getBlankName());
            newOrder.setIfsc(originalOrder.getIfsc());
            newOrder.setWithdrawType(originalOrder.getWithdrawType());
            newOrder.setState(STATE_APPROVED); // 审核通过
            newOrder.setOrderno(newOrderNo);
            newOrder.setMerchantid(originalOrder.getMerchantid());
            newOrder.setChannelid(originalOrder.getChannelid());
            newOrder.setAgent(originalOrder.getAgent());
            newOrder.setAgentName(originalOrder.getAgentName());
            newOrder.setSalesmanid(originalOrder.getSalesmanid());
            newOrder.setSalesmanName(originalOrder.getSalesmanName());
            newOrder.setLiebian(originalOrder.getLiebian());
            newOrder.setCreateTime(new Date());
            newOrder.setWithdrawTime(new Date());
            newOrder.setStateTime(new Date());
            newOrder.setRemark("重新申请提现");
            newOrder.setOperCode(originalOrder.getOperCode());
            
            // 保存新订单
            withdrawOrderDao.insert(newOrder);

            // 冻结用户提现金额
            freezeUserWithdrawBalance(user, originalOrder);
            
            log.info("成功创建新的提现申请订单 - 原订单号: {}, 新订单号: {}, 用户ID: {}, 金额: {}", 
                    originalOrder.getOrderno(), newOrderNo, user.getId(), originalOrder.getAmount());
                    
        } catch (Exception e) {
            log.error("创建新提现订单失败 - 原订单号: {}, 用户ID: {}, 错误信息: {}", 
                     originalOrder.getOrderno(), user.getId(), e.getMessage(), e);
            throw new RuntimeException("创建新提现订单失败: " + e.getMessage());
        }
    }

    /**
     * 冻结用户提现金额
     */
    private void freezeUserWithdrawBalance(MemberEntity user, WithdrawOrderEntity order) {
        try {
            Long amount = order.getAmount();
            Integer withdrawType = order.getWithdrawType();
            
            log.info("开始冻结用户提现金额 - 用户ID: {}, 金额: {}, 提现类型: {}", 
                    user.getId(), amount, withdrawType);
            
            // 根据提现类型冻结相应的资金
            switch (withdrawType) {
                case 1: // 余额提现
                    // 检查可提现余额是否足够
                    if (user.getCashwithdrawable() == null || user.getCashwithdrawable() < amount) {
                        throw new RenException("可提现余额不足，无法冻结资金");
                    }
                    // 从可提现余额扣除，增加到冻结余额
                    Long oldCashWithdrawable = user.getCashwithdrawable();
                    Long oldFreezeBalance = user.getFreezeBalance() != null ? user.getFreezeBalance() : 0L;
                    
                    user.setCashwithdrawable(oldCashWithdrawable - amount);
                    user.setFreezeBalance(oldFreezeBalance + amount);
                    log.info("余额提现冻结成功 - 用户ID: {}, 金额: {}, 可提现余额: {}, 冻结余额: {}", 
                            user.getId(), amount, user.getCashwithdrawable(), user.getFreezeBalance());
                    break;
                    
                case 2: // 佣金提现
                    // 检查佣金余额是否足够
                    if (user.getCommissionBalance() == null || user.getCommissionBalance() < amount) {
                        throw new RenException("佣金余额不足，无法冻结资金");
                    }
                    // 从佣金余额扣除，增加到冻结余额
                    Long oldCommissionBalance = user.getCommissionBalance();
                    Long oldFreezeBalance2 = user.getFreezeBalance() != null ? user.getFreezeBalance() : 0L;
                    
                    user.setCommissionBalance(oldCommissionBalance - amount);
                    user.setFreezeBalance(oldFreezeBalance2 + amount);
                    log.info("佣金提现冻结成功 - 用户ID: {}, 金额: {}, 佣金余额: {}, 冻结余额: {}", 
                            user.getId(), amount, user.getCommissionBalance(), user.getFreezeBalance());
                    break;
                    
                default:
                    log.warn("未知的提现类型: {} - 订单号: {}", withdrawType, order.getOrderno());
                    throw new RenException("未知的提现类型: " + withdrawType);
            }
            
            // 更新用户信息
            int updateResult = memberDao.updateById(user);
            if (updateResult <= 0) {
                log.error("更新用户钱包余额失败 - 用户ID: {}", user.getId());
                throw new RenException("更新用户钱包余额失败");
            }
            
            log.info("用户提现金额冻结完成 - 用户ID: {}, 提现类型: {}, 金额: {}", 
                    user.getId(), withdrawType, amount);
                    
        } catch (Exception e) {
            log.error("冻结用户提现金额失败 - 用户ID: {}, 金额: {}, 提现类型: {}, 错误信息: {}", 
                     user.getId(), order.getAmount(), order.getWithdrawType(), e.getMessage(), e);
            throw new RuntimeException("冻结用户提现金额失败: " + e.getMessage());
        }
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        long timestamp = System.currentTimeMillis();
        long sequence = orderNoGenerator.incrementAndGet();
        return "RW" + timestamp + String.format("%04d", sequence % 10000);
    }

    /**
     * 处理提现驳回逻辑
     */
    private void handleWithdrawRejection(WithdrawOrderEntity withdrawOrder, Long withdrawAmount) {
        try {
            log.info("提现驳回，需要将资金 {} 退回给用户 {}", withdrawAmount, withdrawOrder.getUserId());

            WithdrawOrderEntity order = withdrawOrderDao.selectByOrderno(withdrawOrder.getOrderno());
            MemberEntity user = memberDao.selectById(Long.valueOf(order.getUserId()));

            // 解冻资金，返还到可用余额（一条SQL完成）
            int result = memberDao.updateBalanceOnWithdrawFailure(
                Long.valueOf(order.getUserId()), 
                order.getAmount(), 
                order.getWithdrawType()
            );
            
            if (result > 0) {
                String withdrawTypeName = order.getWithdrawType() == 1 ? "余额提现" : "佣金提现";
                log.info("提现驳回，已回退{}金额 - 用户ID: {}, 金额: {}", 
                        withdrawTypeName, order.getUserId(), order.getAmount());
            } else {
                log.warn("提现驳回回退金额失败 - 用户ID: {}, 金额: {}, 影响行数: {}", 
                        order.getUserId(), order.getAmount(), result);
            }

            // 更新订单状态
            order.setState(STATE_REJECTED); // 驳回
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
        // 只有待审核状态的订单才能进行审核
        return currentState != null && currentState == STATE_PENDING;
    }

    /**
     * 根据审核状态获取相应的消息
     */
    private String getStatusMessage(Integer state) {
        switch (state) {
            case STATE_APPROVED:
                return "审核通过";
            case STATE_WITHDRAWN:
                return "已提现";
            case STATE_REJECTED:
                return "驳回";
            case STATE_FAILED:
                return "提现失败";
            case STATE_INVALID:
                return "无效订单";
            default:
                return null;
        }
    }

    /**
     * 记录余额明细
     */
    private void recordBalanceDetail(WithdrawOrderEntity withdrawOrder, MemberEntity user, Long amountInCents, String remark) {
        try {
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(Long.valueOf(withdrawOrder.getUserId()));
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(user.getAgent());
            balanceDetail.setAgentName(user.getAgentName());
            //2 余额提现 33佣金提现
            if (withdrawOrder.getWithdrawType() == 1) {
                balanceDetail.setBusiType(2);
            } else {
                balanceDetail.setBusiType(33);
            }

            balanceDetail.setChannel("1");
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