package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.annotation.LoginUser;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.dto.PlaceOrderDTO;
import io.renren.entity.UserEntity;
import io.renren.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 项目相关接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@RestController
@RequestMapping("/api/project")
@Api(tags = "项目相关接口")
public class ApiProjectController {
    
    @Autowired
    private OrderService orderService;
    
    @Login
    @PostMapping("placeAnOrder")
    @ApiOperation("下单")
    public Result<Map<String, String>> placeOrder(@RequestBody PlaceOrderDTO dto, @LoginUser UserEntity user) {
        try {
            // 参数验证
            ValidatorUtils.validateEntity(dto);
            
            // 调用下单服务
            Map<String, String> result = orderService.placeOrder(dto, user.getId());
            
            if ("success".equals(result.get("status"))) {
                return new Result<Map<String, String>>().ok(result);
            } else {
                return new Result<Map<String, String>>().error(result.get("message"));
            }
            
        } catch (Exception e) {
            return new Result<Map<String, String>>().error("下单失败: " + e.getMessage());
        }
    }
}
