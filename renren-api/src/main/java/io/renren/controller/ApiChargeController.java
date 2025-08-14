package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.ChargeOrderDetailDTO;
import io.renren.dto.ChargeRequestDTO;
import io.renren.dto.ChargePageData;
import io.renren.dto.ChargeResponseDTO;
import io.renren.entity.UserEntity;
import io.renren.enums.ChargeTypeEnum;
import io.renren.service.ChargeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
    @ApiOperation("获取usdt充值订单详情")
    public Result<ChargeOrderDetailDTO> getChargeOrderDetail(@ApiParam(value = "订单号", required = true) @RequestParam String orderNo) {
        try {
            // 根据订单号获取充值订单详情
            ChargeOrderDetailDTO detail = chargeOrderService.getChargeOrderDetailByOrderNo(orderNo);
            return new Result<ChargeOrderDetailDTO>().ok(detail);
            
        } catch (Exception e) {
            return new Result<ChargeOrderDetailDTO>().error("获取充值订单详情失败: " + e.getMessage());
        }
    }

    @Login
    @PostMapping("charge")
    @ApiOperation("前端充值")
    public Result<ChargeResponseDTO> charge(
            @ApiParam(value = "充值金额", required = true) @RequestParam Long amount,
            @ApiParam(value = "充值类型 1银行卡 2虚拟币 3 upi 4 Paytm", required = true) @RequestParam Integer chargeType,
            @ApiParam(value = "支付通道主键") @RequestParam(required = false) Long channelid,
            @LoginUser UserEntity user) {
        try {
            // 参数验证
            if (amount == null || amount <= 0) {
                return new Result<ChargeResponseDTO>().error("充值金额必须大于0");
            }
            if (!ChargeTypeEnum.isValid(chargeType)) {
                return new Result<ChargeResponseDTO>().error("充值类型无效");
            }

            // 创建充值订单
            String orderno = chargeOrderService.createChargeOrder(user.getId(), amount, chargeType, channelid);
            
            // 构建充值响应数据
            ChargeResponseDTO responseDTO = new ChargeResponseDTO();
            responseDTO.setOrderNo(orderno);
            responseDTO.setPOrderNo(orderno);
            //三方返回的u数量 TODO 等接口返回
            responseDTO.setUamount(BigDecimal.ZERO);
            
            // 构建支付链接
            String payUrl = buildPayUrl(orderno, amount, String.valueOf(channelid));
            responseDTO.setPayUrl(payUrl);
            
            // 生成商户号
            String merchantNo = generateMerchantNo();
            responseDTO.setMerchantNo(merchantNo);

            return new Result<ChargeResponseDTO>().ok(responseDTO);
            
        } catch (Exception e) {
            return new Result<ChargeResponseDTO>().error("充值失败: " + e.getMessage());
        }
    }

    @Login
    @GetMapping("page")
    @ApiOperation("资金明细分页查询")
    public Result<ChargePageData> getChargePage(
            @ApiParam(value = "当前页码，从1开始", required = true) @RequestParam Integer page,
            @ApiParam(value = "每页显示记录数", required = true) @RequestParam Integer limit,
            @LoginUser UserEntity user) {
        try {
            // 参数验证
            if (page == null || page < 1) {
                return new Result<ChargePageData>().error("页码必须大于0");
            }
            if (limit == null || limit < 1 || limit > 100) {
                return new Result<ChargePageData>().error("每页记录数必须在1-100之间");
            }

            // 获取分页数据
            ChargePageData pageData = chargeOrderService.getChargePageData(user.getId(), page, limit);
            
            return new Result<ChargePageData>().ok(pageData);
            
        } catch (Exception e) {
            return new Result<ChargePageData>().error("获取资金明细失败: " + e.getMessage());
        }
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        // 生成订单号逻辑，格式：TP + 年月日时分秒 + 3位随机数
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        String random = String.format("%03d", new Random().nextInt(1000));
        return "TP" + timestamp + random;
    }

    /**
     * 构建支付链接
     */
    private String buildPayUrl(String orderNo, Long amount, String channelid) {
        // 这里需要根据实际的支付网关配置来构建支付链接
        // 示例：https://pay-v2.bankkpay.com/cashier?orderId=订单号&amount=金额
        String baseUrl = "https://pay-v2.bankkpay.com/cashier";
        return String.format("%s?orderId=%s&amount=%d", baseUrl, orderNo, amount);
    }

    /**
     * 生成商户号
     */
    private String generateMerchantNo() {
        // 生成商户号逻辑，格式：R + 年月日时分秒 + 4位随机数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        String random = String.format("%04d", new Random().nextInt(10000));
        return "R" + timestamp + random;
    }
}
