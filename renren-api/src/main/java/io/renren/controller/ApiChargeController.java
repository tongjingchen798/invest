package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.exception.RenException;
import io.renren.common.utils.Result;
import io.renren.dao.PayChannelDao;
import io.renren.dao.PayMerchantDao;
import io.renren.dto.ChargeOrderDetailDTO;
import io.renren.dto.ChargePageData;
import io.renren.dto.ChargeResponseDTO;
import io.renren.dto.PaymentResponseDTO;
import io.renren.entity.PayChannelEntity;
import io.renren.entity.PayMerchantEntity;
import io.renren.entity.UserEntity;
import io.renren.enums.ChargeTypeEnum;
import io.renren.service.ChargeOrderService;
import io.renren.service.WePayPaymentService;
import io.renren.utils.OkHttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    @Autowired
    private PayChannelDao payChannelDao;

    @Autowired
    private PayMerchantDao payMerchantDao;

    @Autowired
    private OkHttpUtil okHttpUtil;
    
    @Autowired
    private WePayPaymentService wePayPaymentService;

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
            @ApiParam(value = "充值类型 1银行卡 2虚拟币 3 upi 4 Paytm", required = true) @RequestParam Integer charge_type,
            @ApiParam(value = "支付通道主键") @RequestParam(required = false) Long channelid,
            @LoginUser UserEntity user) {

        try {
            // 参数验证
            if (amount == null || amount <= 0) {
                throw new RenException(500, "金额不能小于0");
            }
            if (!ChargeTypeEnum.isValid(charge_type)) {
                throw new RenException(500, "充值类型无效");
            }
            PayChannelEntity payChannelEntity = payChannelDao.selectById(channelid);
            if (payChannelEntity == null) {
                throw new RenException(500, "通道已关闭");
            }
            PayMerchantEntity payMerchantEntity = payMerchantDao.selectById(payChannelEntity.getMerchantid());
            if (payMerchantEntity == null) {
                throw new RenException(500, "商户已停用");
            }

            // 创建充值订单
            String orderno = chargeOrderService.createChargeOrder(user.getId(), amount, charge_type, channelid);
            //分转换为元
            amount=amount/100;
            // 构建充值响应数据
            ChargeResponseDTO responseDTO = new ChargeResponseDTO();
            responseDTO.setOrderNo(orderno);
            responseDTO.setMerchantNo(payMerchantEntity.getMerchantno());
            responseDTO.setAmount(amount);

            // 根据充值类型设置不同的响应数据
            ChargeTypeEnum chargeTypeEnum = ChargeTypeEnum.getByCode(charge_type);
            if (chargeTypeEnum != null) {
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

                        // 调用WePay支付服务创建支付订单
                        PaymentResponseDTO paymentResponse = wePayPaymentService.createPaymentOrder(
                                user, amount, orderno, payChannelEntity, payMerchantEntity);

                        // 设置支付地址
                        String payUrl = "";
                        if (paymentResponse.getData() != null) {
                            payUrl = paymentResponse.getData().getPayUrl();
                        }
                        responseDTO.setBankCardInfo(payUrl);
                        responseDTO.setPOrderNo(paymentResponse.getData().getTradeNo());
                        responseDTO.setErrorCode(0);
                        break;
                }
            }
            responseDTO.setFlag(1); //2是内部
            return new Result<ChargeResponseDTO>().ok(responseDTO);

        } catch (Exception e) {
            throw new RenException(500, "充值失败");
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
     * 生成参数签名
     * 
     * @param params 请求参数
     * @param secretKey 商户密钥
     * @return 签名字符串
     */
    private String generateSign(Map<String, Object> params, String secretKey) {
        try {
            // 移除sign参数
            Map<String, Object> signParams = new HashMap<>(params);
            signParams.remove("sign");
            
            // 按参数名排序
            StringBuilder sb = new StringBuilder();
            signParams.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                    });
            
            // 添加密钥
            sb.append("key=").append(secretKey);
            
            // 生成MD5签名
            String signStr = sb.toString();
            return org.apache.commons.codec.digest.DigestUtils.md5Hex(signStr).toUpperCase();
            
        } catch (Exception e) {
            throw new RenException(500, "签名生成失败: " + e.getMessage());
        }
    }

}
