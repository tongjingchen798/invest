package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.ChargeOrderDetailDTO;
import io.renren.dto.ChargeRequestDTO;
import io.renren.entity.UserEntity;
import io.renren.enums.ChargeTypeEnum;
import io.renren.service.ChargeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 充值订单接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/api/charge")
@Api(tags = "充值订单接口")
public class ApiChargeController {
    
    @Autowired
    private ChargeOrderService chargeOrderService;
    
    @Login
    @PostMapping("orderdetail")
    @ApiOperation("获取充值订单详情")
    public Result<ChargeOrderDetailDTO> getChargeOrderDetail(@LoginUser UserEntity user) {
        try {
            // 获取用户充值订单详情
            ChargeOrderDetailDTO detail = chargeOrderService.getChargeOrderDetail(user.getId());
            return new Result<ChargeOrderDetailDTO>().ok(detail);
            
        } catch (Exception e) {
            return new Result<ChargeOrderDetailDTO>().error("获取充值订单详情失败: " + e.getMessage());
        }
    }

    @Login
    @PostMapping("charge")
    @ApiOperation("前端充值")
    public Result<String> charge(
            @ApiParam(value = "充值金额", required = true) @RequestParam Long amount,
            @ApiParam(value = "充值类型 1银行卡 2虚拟币 3 upi 4 Paytm", required = true) @RequestParam Integer chargeType,
            @ApiParam(value = "支付通道主键") @RequestParam(required = false) Long channelid,
            @LoginUser UserEntity user) {
        try {
            // 参数验证
            if (amount == null || amount <= 0) {
                return new Result<String>().error("充值金额必须大于0");
            }
            if (!ChargeTypeEnum.isValid(chargeType)) {
                return new Result<String>().error("充值类型无效");
            }

            // 创建充值订单
            String orderno = chargeOrderService.createChargeOrder(user.getId(), amount, chargeType, channelid);
            //TODO 返回还没确认 应该返回支付url 或者 支付二维码
            return new Result<String>().ok(orderno);
            
        } catch (Exception e) {
            return new Result<String>().error("充值失败: " + e.getMessage());
        }
    }
}
