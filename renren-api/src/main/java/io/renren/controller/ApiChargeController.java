package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.Result;
import io.renren.dto.ChargeOrderDetailDTO;
import io.renren.dto.ChargePageData;
import io.renren.dto.ChargeResponseDTO;
import io.renren.entity.UserEntity;
import io.renren.enums.ChargeTypeEnum;
import io.renren.service.ChargeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 充值订单接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Slf4j
@RestController
@RequestMapping("/api/charge")
@Api(tags = "充值订单接口")
public class ApiChargeController {

    @Autowired
    private ChargeOrderService chargeOrderService;


    @Login
    @PostMapping("orderdetail")
    @ApiOperation("获取usdt充值订单详情")
    public Result<ChargeOrderDetailDTO> getChargeOrderDetail(@ApiParam(value = "订单号", required = true) @RequestParam String order_no) {
        try {
            // 根据订单号获取充值订单详情
            ChargeOrderDetailDTO detail = chargeOrderService.getChargeOrderDetailByOrderNo(order_no);
            return new Result<ChargeOrderDetailDTO>().ok(detail);

        } catch (Exception e) {
            log.error("获取充值订单详情失败: {}", e.getMessage(), e);
            throw new RenException(ErrorCode.CHARGE_ORDER_DETAIL_FAILED);
        }
    }

    @Login
    @PostMapping("charge")
    @ApiOperation("前端充值")
    public Result<ChargeResponseDTO> charge(
            @ApiParam(value = "充值金额", required = true) @RequestParam Long amount,
            @ApiParam(value = "充值类型 1银行卡 2虚拟币 3 upi 4 Paytm", required = true) @RequestParam Integer charge_type,
            @ApiParam(value = "支付通道主键") @RequestParam(required = false) Long channelid,
            @LoginUser UserEntity user) {

        try {
            // 参数验证
            if (amount == null || amount <= 0) {
                throw new RenException(ErrorCode.CHARGE_AMOUNT_INVALID);
            }
            if (!ChargeTypeEnum.isValid(charge_type)) {
                throw new RenException(ErrorCode.CHARGE_TYPE_INVALID);
            }
            // 创建充值订单
            ChargeResponseDTO responseDTO = chargeOrderService.createChargeOrder(user.getId(), amount, charge_type, channelid);

            return new Result<ChargeResponseDTO>().ok(responseDTO);

        } catch (Exception e) {
            log.error("充值失败: {}", e.getMessage(), e);
            throw new RenException(ErrorCode.CHARGE_FAILED);
        }
    }

    @Login
    @GetMapping("page")
    @ApiOperation("充值记录分页查询")
    public Result<ChargePageData> getChargePage(
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam Integer page,
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam Integer limit,
            @LoginUser UserEntity user) {
        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<ChargePageData>().ok(null);
            }
            if (limit == null || limit < 1 || limit > 100) {
                return new Result<ChargePageData>().ok(null);
            }

            // 获取分页数据
            ChargePageData pageData = chargeOrderService.getChargePageData(user.getId(), page, limit);

            return new Result<ChargePageData>().ok(pageData);

        } catch (Exception e) {
            log.error("获取资金明细失败: {}", e.getMessage(), e);
            throw new RenException(ErrorCode.CHARGE_PAGE_DATA_FAILED);
        }
    }


}
