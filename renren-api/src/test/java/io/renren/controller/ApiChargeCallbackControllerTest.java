package io.renren.controller;

import io.renren.common.utils.Result;
import io.renren.service.ChargeCallbackService;
import io.renren.service.ChargeOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 充值回调接口控制器测试类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@ExtendWith(MockitoExtension.class)
public class ApiChargeCallbackControllerTest {

    @Mock
    private ChargeOrderService chargeOrderService;

    @Mock
    private ChargeCallbackService chargeCallbackService;

    @InjectMocks
    private ApiChargeCallbackController apiChargeCallbackController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(apiChargeCallbackController).build();
    }

    @Test
    void testBankCallback_Success() throws Exception {
        // 准备测试数据
        String orderNo = "CHG123456789";
        String thirdOrderNo = "BANK987654321";
        String amount = "100.00";
        String status = "SUCCESS";
        String sign = "valid_sign_123";

        // Mock服务层方法
        when(chargeCallbackService.processBankCallback(
                eq(thirdOrderNo), eq(orderNo), eq(amount), 
                eq(status), anyString(), eq(sign), anyMap()))
                .thenReturn(true);

        // 执行测试
        mockMvc.perform(post("/api/charge/callback/bank")
                .param("thirdOrderNo", thirdOrderNo)
                .param("orderNo", orderNo)
                .param("amount", amount)
                .param("status", status)
                .param("sign", sign)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));

        // 验证服务层方法被调用
        verify(chargeCallbackService, times(1)).processBankCallback(
                eq(thirdOrderNo), eq(orderNo), eq(amount), 
                eq(status), anyString(), eq(sign), anyMap());
    }

    @Test
    void testHealthCheck() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/charge/callback/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}
