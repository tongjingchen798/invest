package io.renren.modules.withdraw.service;

import io.renren.modules.withdraw.dto.WithdrawAuditDTO;

/**
 * 管理员提现服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface AdminWithdrawService {

    /**
     * 审核提现申请
     *
     * @param auditDTO 审核参数
     */
    void auditWithdraw(WithdrawAuditDTO auditDTO);

    /**
     * 提现冲正
     * 冲正是三方打款被退回了，三方回给你账户加余额，我们就需要手动给客户加钱，以工资的形式
     *
     * @param orderId 提现订单ID
     * @param operatorId 操作人ID
     */
    void withdrawCorrect(Long orderId, Long operatorId);
}
