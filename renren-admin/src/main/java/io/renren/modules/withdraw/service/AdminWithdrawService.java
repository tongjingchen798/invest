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
}
