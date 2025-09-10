package io.renren.common.utils;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Pattern;

/**
 * 印度短信号码验证工具类
 * 
 * @author lip
 * @date 2024-01-01
 */
public class IndianSmsValidator {
    
    /**
     * 印度手机号码正则表达式
     */
    private static final Pattern INDIAN_MOBILE_PATTERN = Pattern.compile(
        "^(\\+?91|91)?[6-9]\\d{9}$"
    );
    
    /**
     * 验证是否为印度短信号码
     * 
     * @param mobile 手机号码
     * @return 是否为印度短信号码
     */
    public static boolean isValidIndianMobile(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return false;
        }
        
        // 去除空格和特殊字符
        String cleanMobile = mobile.trim().replaceAll("[\\s-]", "");
        
        // 验证手机号码格式
        return INDIAN_MOBILE_PATTERN.matcher(cleanMobile).matches();
    }
    
    /**
     * 格式化印度手机号码
     * 
     * @param mobile 手机号码
     * @return 格式化后的手机号码（+91xxxxxxxxxx）
     */
    public static String formatIndianMobile(String mobile) {
        if (!isValidIndianMobile(mobile)) {
            return mobile;
        }
        
        // 去除空格和特殊字符
        String cleanMobile = mobile.trim().replaceAll("[\\s-]", "");
        
        // 提取10位数字
        String digits = cleanMobile.replaceAll("[^0-9]", "");
        if (digits.startsWith("91") && digits.length() == 12) {
            digits = digits.substring(2);
        }
        
        return "+91" + digits;
    }
}
