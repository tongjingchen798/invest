//package io.renren.controller;
//
//import io.renren.common.utils.Result;
//import io.renren.entity.ChargeOrderEntity;
//import io.renren.service.ChargeOrderService;
//import io.renren.service.ChargeCallbackService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Map;
//
///**
// * 充值回调接口控制器
// * 处理第三方支付平台的回调通知
// *
// * @author renren
// * @email renren@gmail.com
// * @date 2024-01-01 00:00:00
// */
//@Slf4j
//@RestController
//@RequestMapping("/api/charge/callback")
//@Api(tags = "充值回调接口")
//public class ApiChargeCallbackController {
//
//    @Autowired
//    private ChargeOrderService chargeOrderService;
//
//    @Autowired
//    private ChargeCallbackService chargeCallbackService;
//
//    /**
//     * 银行卡充值回调
//     * 处理银行卡支付成功后的回调通知
//     */
//    @PostMapping("/bank")
//    @ApiOperation("银行卡充值回调")
//    public String bankCallback(
//            @ApiParam(value = "第三方订单号") @RequestParam(required = false) String thirdOrderNo,
//            @ApiParam(value = "平台订单号") @RequestParam(required = false) String orderNo,
//            @ApiParam(value = "支付金额") @RequestParam(required = false) String amount,
//            @ApiParam(value = "支付状态") @RequestParam(required = false) String status,
//            @ApiParam(value = "支付时间") @RequestParam(required = false) String payTime,
//            @ApiParam(value = "签名") @RequestParam(required = false) String sign,
//            HttpServletRequest request) {
//
//        log.info("收到银行卡充值回调，参数：thirdOrderNo={}, orderNo={}, amount={}, status={}, payTime={}, sign={}",
//                thirdOrderNo, orderNo, amount, status, payTime, sign);
//
//        try {
//            // 获取所有请求参数
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            log.debug("银行卡回调完整参数：{}", parameterMap);
//
//            // 处理银行卡充值回调
//            boolean success = chargeCallbackService.processBankCallback(thirdOrderNo, orderNo, amount, status, payTime, sign, parameterMap);
//
//            if (success) {
//                log.info("银行卡充值回调处理成功，订单号：{}", orderNo);
//                return "SUCCESS"; // 返回成功标识
//            } else {
//                log.warn("银行卡充值回调处理失败，订单号：{}", orderNo);
//                return "FAIL";
//            }
//
//        } catch (Exception e) {
//            log.error("处理银行卡充值回调异常，订单号：{}", orderNo, e);
//            return "ERROR";
//        }
//    }
//
//    /**
//     * 虚拟币充值回调
//     * 处理USDT等虚拟币支付成功后的回调通知
//     */
//    @PostMapping("/crypto")
//    @ApiOperation("虚拟币充值回调")
//    public String cryptoCallback(
//            @ApiParam(value = "第三方订单号") @RequestParam(required = false) String thirdOrderNo,
//            @ApiParam(value = "平台订单号") @RequestParam(required = false) String orderNo,
//            @ApiParam(value = "USDT数量") @RequestParam(required = false) String uAmount,
//            @ApiParam(value = "支付状态") @RequestParam(required = false) String status,
//            @ApiParam(value = "交易哈希") @RequestParam(required = false) String txHash,
//            @ApiParam(value = "钱包地址") @RequestParam(required = false) String walletAddr,
//            @ApiParam(value = "签名") @RequestParam(required = false) String sign,
//            HttpServletRequest request) {
//
//        log.info("收到虚拟币充值回调，参数：thirdOrderNo={}, orderNo={}, uAmount={}, status={}, txHash={}, walletAddr={}, sign={}",
//                thirdOrderNo, orderNo, uAmount, status, txHash, walletAddr, sign);
//
//        try {
//            // 获取所有请求参数
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            log.debug("虚拟币回调完整参数：{}", parameterMap);
//
//            // 处理虚拟币充值回调
//            boolean success = chargeCallbackService.processCryptoCallback(thirdOrderNo, orderNo, uAmount, status, txHash, walletAddr, sign, parameterMap);
//
//            if (success) {
//                log.info("虚拟币充值回调处理成功，订单号：{}", orderNo);
//                return "SUCCESS";
//            } else {
//                log.warn("虚拟币充值回调处理失败，订单号：{}", orderNo);
//                return "FAIL";
//            }
//
//        } catch (Exception e) {
//            log.error("处理虚拟币充值回调异常，订单号：{}", orderNo, e);
//            return "ERROR";
//        }
//    }
//
//    /**
//     * UPI充值回调
//     * 处理UPI支付成功后的回调通知
//     */
//    @PostMapping("/upi")
//    @ApiOperation("UPI充值回调")
//    public String upiCallback(
//            @ApiParam(value = "第三方订单号") @RequestParam(required = false) String thirdOrderNo,
//            @ApiParam(value = "平台订单号") @RequestParam(required = false) String orderNo,
//            @ApiParam(value = "支付金额") @RequestParam(required = false) String amount,
//            @ApiParam(value = "支付状态") @RequestParam(required = false) String status,
//            @ApiParam(value = "UPI ID") @RequestParam(required = false) String upiId,
//            @ApiParam(value = "签名") @RequestParam(required = false) String sign,
//            HttpServletRequest request) {
//
//        log.info("收到UPI充值回调，参数：thirdOrderNo={}, orderNo={}, amount={}, status={}, upiId={}, sign={}",
//                thirdOrderNo, orderNo, amount, status, upiId, sign);
//
//        try {
//            // 获取所有请求参数
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            log.debug("UPI回调完整参数：{}", parameterMap);
//
//            // 处理UPI充值回调
//            boolean success = chargeCallbackService.processUpiCallback(thirdOrderNo, orderNo, amount, status, upiId, sign, parameterMap);
//
//            if (success) {
//                log.info("UPI充值回调处理成功，订单号：{}", orderNo);
//                return "SUCCESS";
//            } else {
//                log.warn("UPI充值回调处理失败，订单号：{}", orderNo);
//                return "FAIL";
//            }
//
//        } catch (Exception e) {
//            log.error("处理UPI充值回调异常，订单号：{}", orderNo, e);
//            return "ERROR";
//        }
//    }
//
//    /**
//     * Paytm充值回调
//     * 处理Paytm支付成功后的回调通知
//     */
//    @PostMapping("/paytm")
//    @ApiOperation("Paytm充值回调")
//    public String paytmCallback(
//            @ApiParam(value = "第三方订单号") @RequestParam(required = false) String thirdOrderNo,
//            @ApiParam(value = "平台订单号") @RequestParam(required = false) String orderNo,
//            @ApiParam(value = "支付金额") @RequestParam(required = false) String amount,
//            @ApiParam(value = "支付状态") @RequestParam(required = false) String status,
//            @ApiParam(value = "Paytm订单ID") @RequestParam(required = false) String paytmOrderId,
//            @ApiParam(value = "签名") @RequestParam(required = false) String sign,
//            HttpServletRequest request) {
//
//        log.info("收到Paytm充值回调，参数：thirdOrderNo={}, orderNo={}, amount={}, status={}, paytmOrderId={}, sign={}",
//                thirdOrderNo, orderNo, amount, status, paytmOrderId, sign);
//
//        try {
//            // 获取所有请求参数
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            log.debug("Paytm回调完整参数：{}", parameterMap);
//
//            // 处理Paytm充值回调
//            boolean success = chargeCallbackService.processPaytmCallback(thirdOrderNo, orderNo, amount, status, paytmOrderId, sign, parameterMap);
//
//            if (success) {
//                log.info("Paytm充值回调处理成功，订单号：{}", orderNo);
//                return "SUCCESS";
//            } else {
//                log.warn("Paytm充值回调处理失败，订单号：{}", orderNo);
//                return "FAIL";
//            }
//
//        } catch (Exception e) {
//            log.error("处理Paytm充值回调异常，订单号：{}", orderNo, e);
//            return "ERROR";
//        }
//    }
//
//    /**
//     * 通用充值回调接口
//     * 处理各种支付方式的回调通知
//     */
//    @PostMapping("/common")
//    @ApiOperation("通用充值回调")
//    public String commonCallback(
//            @ApiParam(value = "支付方式") @RequestParam(required = false) String paymentMethod,
//            @ApiParam(value = "第三方订单号") @RequestParam(required = false) String thirdOrderNo,
//            @ApiParam(value = "平台订单号") @RequestParam(required = false) String orderNo,
//            @ApiParam(value = "支付金额") @RequestParam(required = false) String amount,
//            @ApiParam(value = "支付状态") @RequestParam(required = false) String status,
//            @ApiParam(value = "签名") @RequestParam(required = false) String sign,
//            HttpServletRequest request) {
//
//        log.info("收到通用充值回调，参数：paymentMethod={}, thirdOrderNo={}, orderNo={}, amount={}, status={}, sign={}",
//                paymentMethod, thirdOrderNo, orderNo, amount, status, sign);
//
//        try {
//            // 获取所有请求参数
//            Map<String, String[]> parameterMap = request.getParameterMap();
//            log.debug("通用回调完整参数：{}", parameterMap);
//
//            // 处理通用充值回调
//            boolean success = chargeCallbackService.processCommonCallback(paymentMethod, thirdOrderNo, orderNo, amount, status, sign, parameterMap);
//
//            if (success) {
//                log.info("通用充值回调处理成功，订单号：{}", orderNo);
//                return "SUCCESS";
//            } else {
//                log.warn("通用充值回调处理失败，订单号：{}", orderNo);
//                return "FAIL";
//            }
//
//        } catch (Exception e) {
//            log.error("处理通用充值回调异常，订单号：{}", orderNo, e);
//            return "ERROR";
//        }
//    }
//
//    /**
//     * 查询充值订单状态
//     * 用于第三方支付平台查询订单状态
//     */
//    @GetMapping("/query")
//    @ApiOperation("查询充值订单状态")
//    public Result<Map<String, Object>> queryOrderStatus(
//            @ApiParam(value = "平台订单号", required = true) @RequestParam String orderNo) {
//
//        try {
//            log.info("查询充值订单状态，订单号：{}", orderNo);
//
//            // 查询订单信息
//            ChargeOrderEntity order = chargeOrderService.getChargeOrderByOrderno(orderNo);
//            if (order == null) {
//                return new Result<Map<String, Object>>().error("订单不存在");
//            }
//
//            // 构建返回数据
//            Map<String, Object> result = chargeCallbackService.buildOrderStatusResponse(order);
//
//            return new Result<Map<String, Object>>().ok(result);
//
//        } catch (Exception e) {
//            log.error("查询充值订单状态异常，订单号：{}", orderNo, e);
//            return new Result<Map<String, Object>>().error("查询订单状态失败：" + e.getMessage());
//        }
//    }
//
//    /**
//     * 健康检查接口
//     * 用于第三方支付平台检查回调接口是否可用
//     */
//    @GetMapping("/health")
//    @ApiOperation("回调接口健康检查")
//    public String healthCheck() {
//        log.debug("回调接口健康检查");
//        return "OK";
//    }
//}
