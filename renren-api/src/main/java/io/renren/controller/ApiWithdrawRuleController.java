//package io.renren.controller;
//
//import io.renren.common.utils.Result;
//import io.renren.config.WithdrawConfig;
//import io.renren.utils.WithdrawRuleValidator;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 提现规则查询接口
// *
// * @author renren
// * @since 1.0.0
// */
//@RestController
//@RequestMapping("/api/withdraw")
//@Api(tags = "提现规则接口")
//public class ApiWithdrawRuleController {
//
//    @Autowired
//    private WithdrawConfig withdrawConfig;
//
//    @Autowired
//    private WithdrawRuleValidator withdrawRuleValidator;
//
//    @GetMapping("rules")
//    @ApiOperation("获取提现规则")
//    public Result<Map<String, Object>> getWithdrawRules() {
//        try {
//            Map<String, Object> rules = new HashMap<>();
//
//            // 基本规则
//            rules.put("minAmount", withdrawConfig.getMinAmount());
//            rules.put("maxAmount", withdrawConfig.getMaxAmount());
//            rules.put("feeRate", withdrawConfig.getFeeRate());
//            rules.put("percentageFee", withdrawConfig.isPercentageFee());
//            rules.put("fixedFee", withdrawConfig.getFixedFee());
//            rules.put("description", "提现规则说明");
//            rules.put("feeDescription", "手续费按提现金额的" + withdrawConfig.getFeeRate() + "%收取");
//
//            return new Result().ok(rules);
//
//        } catch (Exception e) {
//            return new Result().error("获取提现规则失败：" + e.getMessage());
//        }
//    }
//
//    @GetMapping("calculate")
//    @ApiOperation("计算提现手续费和实际到账金额")
//    public Result<Map<String, Object>> calculateWithdrawFee(
//            @ApiParam(value = "提现金额（卢比）", required = true)
//            @RequestParam("amount") BigDecimal amount) {
//        try {
//            // 验证提现金额
//            WithdrawRuleValidator.ValidationResult validationResult = withdrawRuleValidator.validateAmount(amount);
//            if (!validationResult.isValid()) {
//                return new Result().error(validationResult.getErrorMessage());
//            }
//
//            // 计算手续费和实际到账金额
//            BigDecimal fee = withdrawRuleValidator.calculateFee(amount);
//            BigDecimal realAmount = withdrawRuleValidator.calculateRealAmount(amount);
//
//            Map<String, Object> result = new HashMap<>();
//            result.put("withdrawAmount", amount);
//            result.put("fee", fee);
//            result.put("realAmount", realAmount);
//            result.put("feeRate", withdrawConfig.getFeeRate() + "%");
//            result.put("feeDescription", "手续费按提现金额的" + withdrawConfig.getFeeRate() + "%收取");
//
//            return new Result().ok(result);
//
//        } catch (Exception e) {
//            return new Result().error("计算提现费用失败：" + e.getMessage());
//        }
//    }
//
//    @GetMapping("validate")
//    @ApiOperation("验证提现金额是否符合规则")
//    public Result<Map<String, Object>> validateWithdrawAmount(
//            @ApiParam(value = "提现金额（卢比）", required = true)
//            @RequestParam("amount") BigDecimal amount) {
//        try {
//            // 验证提现金额
//            WithdrawRuleValidator.ValidationResult validationResult = withdrawRuleValidator.validateAmount(amount);
//
//            Map<String, Object> result = new HashMap<>();
//            result.put("valid", validationResult.isValid());
//            result.put("message", validationResult.getErrorMessage());
//            result.put("amount", amount);
//            result.put("minAmount", withdrawConfig.getMinAmount());
//            result.put("maxAmount", withdrawConfig.getMaxAmount());
//
//            if (validationResult.isValid()) {
//                // 如果验证通过，计算手续费和实际到账金额
//                BigDecimal fee = withdrawRuleValidator.calculateFee(amount);
//                BigDecimal realAmount = withdrawRuleValidator.calculateRealAmount(amount);
//                result.put("fee", fee);
//                result.put("realAmount", realAmount);
//            }
//
//            return new Result().ok(result);
//
//        } catch (Exception e) {
//            return new Result().error("验证提现金额失败：" + e.getMessage());
//        }
//    }
//}
