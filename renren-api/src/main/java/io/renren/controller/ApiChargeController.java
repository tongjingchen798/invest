package io.renren.controller;

import com.alibaba.fastjson.JSON;
import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

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
    public Result<String> charge(
            @ApiParam(value = "充值金额", required = true) @RequestParam Long amount,
            @ApiParam(value = "充值类型 1银行卡 2虚拟币 3 upi 4 Paytm", required = true) @RequestParam Integer charge_type,
            @ApiParam(value = "支付通道主键") @RequestParam(required = false) Long channelid,
            @LoginUser UserEntity user) {
        
        try {
            // 参数验证
            if (amount == null || amount <= 0) {
                return new Result<String>().error("充值金额必须大于0");
            }
            if (!ChargeTypeEnum.isValid(charge_type)) {
                return new Result<String>().error("充值类型无效");
            }

            // 创建充值订单
            String orderno = chargeOrderService.createChargeOrder(user.getId(), amount, charge_type, channelid);
            
            // 构建充值响应数据
            ChargeResponseDTO responseDTO = new ChargeResponseDTO();
            responseDTO.setOrderNo(orderno);
            responseDTO.setPOrderNo(orderno);
            responseDTO.setAmount(BigDecimal.valueOf(amount));
            
            // 根据充值类型设置不同的响应数据
            ChargeTypeEnum chargeTypeEnum = ChargeTypeEnum.getByCode(charge_type);
            if (chargeTypeEnum != null) {
                // 设置充值类型信息
                responseDTO.setChargeTypeInfo(charge_type, chargeTypeEnum.getName());
                
                switch (chargeTypeEnum) {
                    case CRYPTO:
                        // 虚拟币充值，设置USDT相关信息
                        responseDTO.setMerchantNo("usdt");
                        responseDTO.setUsdtInfo(
                            "TVsCfPDWy8EZCFBgjzEXRtSZvr5r96w3U3", // USDT地址
                            new BigDecimal("299.951"), // u数量
                            new BigDecimal("97.50"),   // u价格
                            new BigDecimal("299.951")  // 实际支付u数量
                        );
                        // USDT充值不设置payUrl
                        responseDTO.setPayUrl("");
                        break;
                        
                    case UPI:
                    case PAYTM:
                    case BANK_CARD:
                        // 其他支付方式，设置支付相关信息
                        responseDTO.setUamount(BigDecimal.ZERO);
                        responseDTO.setUprice(BigDecimal.ZERO);
                        responseDTO.setURealAmount(BigDecimal.ZERO);
                        
                        // 构建支付链接
                        String payUrl = buildPayUrl(orderno, amount, String.valueOf(channelid));
                        responseDTO.setBankCardInfo(payUrl);
                        
                        // 生成商户号
                        String merchantNo = generateMerchantNo();
                        responseDTO.setMerchantNo(merchantNo);
                        break;
                }
            }
            responseDTO.setErrorCode(0);
            // 将ChargeResponseDTO转换为JSON字符串
            String jsonResponse = convertToJsonString(responseDTO);
            
            return new Result<String>().ok(jsonResponse);
            
        } catch (Exception e) {
            return new Result<String>().error("充值失败: " + e.getMessage());
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
     * 测试ChargeResponseDTO序列化
     */
    @GetMapping("test-serialization")
    @ApiOperation("测试充值响应DTO序列化")
    public Result<String> testSerialization() {
        try {
            // 创建测试数据
            ChargeResponseDTO testDTO = new ChargeResponseDTO();
            testDTO.setOrderNo("TEST" + System.currentTimeMillis());
            testDTO.setPOrderNo("TEST" + System.currentTimeMillis());
            testDTO.setAmount(new BigDecimal("100000"));
            testDTO.setUamount(new BigDecimal("100.00"));
            testDTO.setUprice(new BigDecimal("97.50"));
            testDTO.setURealAmount(new BigDecimal("100.00"));
            testDTO.setPayUrl("https://pay.example.com/test");
            testDTO.setMerchantNo("TEST" + System.currentTimeMillis());
            testDTO.setWalletAddr("TRC20测试地址");
            testDTO.setChargeTypeInfo(1, "银行卡");
            testDTO.setStatusInfo(0, "待支付");
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new Date());
            testDTO.setTimeInfo(currentTime, currentTime);
            
            // 转换为JSON字符串
            String jsonResponse = convertToJsonString(testDTO);
            return new Result<String>().ok(jsonResponse);
            
        } catch (Exception e) {
            return new Result<String>().error("测试序列化失败: " + e.getMessage());
        }
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

    /**
     * 将ChargeResponseDTO转换为JSON字符串
     */
    private String convertToJsonString(ChargeResponseDTO responseDTO) {
        try {
            return JSON.toJSONString(responseDTO);
        } catch (Exception e) {
            // 如果序列化失败，返回错误信息
            return "{\"error\":\"Failed to convert response to JSON: " + e.getMessage() + "\"}";
        }
    }
}
