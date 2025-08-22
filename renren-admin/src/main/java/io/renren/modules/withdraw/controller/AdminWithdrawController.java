package io.renren.modules.withdraw.controller;

import io.renren.common.utils.Result;
import io.renren.modules.withdraw.dto.WithdrawAuditDTO;
import io.renren.modules.withdraw.service.AdminWithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理员提现审核接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/admin/withdraw")
@Api(tags = "管理员提现审核")
public class AdminWithdrawController {

    @Autowired
    private AdminWithdrawService adminWithdrawService;

    @PostMapping("/withdrawSH")
    @ApiOperation("提现审核")
    public Result<Object> auditWithdraw(@RequestBody WithdrawAuditDTO auditDTO, 
                                       HttpServletRequest request) {
        try {
            // 从请求头获取操作人ID
            String loginUserIdStr = request.getHeader("loginUserId");
            if (loginUserIdStr == null || loginUserIdStr.trim().isEmpty()) {
                return new Result<Object>().error("操作人ID不能为空");
            }
            
            Long loginUserId = Long.parseLong(loginUserIdStr);
            auditDTO.setSysUpdateUserId(loginUserId);
            
            // 参数验证
            if (auditDTO.getId() == null) {
                return new Result<Object>().error("提现记录ID不能为空");
            }
            if (auditDTO.getState() == null) {
                return new Result<Object>().error("审核状态不能为空");
            }
            
            // 验证状态值是否合法
            if (!isValidState(auditDTO.getState())) {
                return new Result<Object>().error("无效的审核状态值");
            }
            
            // 执行审核
            adminWithdrawService.auditWithdraw(auditDTO);
            
            return new Result<Object>().ok("审核操作成功");
            
        } catch (NumberFormatException e) {
            return new Result<Object>().error("操作人ID格式错误");
        } catch (Exception e) {
            return new Result<Object>().error("审核操作失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证审核状态值是否合法
     */
    private boolean isValidState(Integer state) {
        return state != null && (state == 1 || state == 2 || state == 3 || state == 5);
    }
}
