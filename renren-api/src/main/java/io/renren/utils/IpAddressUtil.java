package io.renren.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP地址获取工具类
 * 
 * @author renren
 * @since 1.0.0
 */
public class IpAddressUtil {
    
    /**
     * 获取客户端真实IP地址
     * 
     * @return IP地址字符串
     */
    public static String getClientIpAddress() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return getLocalIpAddress();
            }
            
            HttpServletRequest request = attributes.getRequest();
            return getClientIpAddress(request);
        } catch (Exception e) {
            return getLocalIpAddress();
        }
    }
    
    /**
     * 从HttpServletRequest中获取客户端真实IP地址
     * 
     * @param request HttpServletRequest对象
     * @return IP地址字符串
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return getLocalIpAddress();
        }
        
        String ip = null;
        
        // 1. 尝试从X-Forwarded-For头获取（代理服务器转发）
        ip = request.getHeader("X-Forwarded-For");
        if (isValidIp(ip)) {
            // X-Forwarded-For可能包含多个IP，取第一个
            int index = ip.indexOf(",");
            if (index != -1) {
                ip = ip.substring(0, index).trim();
            }
            return ip;
        }
        
        // 2. 尝试从X-Real-IP头获取
        ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 3. 尝试从Proxy-Client-IP头获取
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 4. 尝试从WL-Proxy-Client-IP头获取
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 5. 尝试从HTTP_CLIENT_IP头获取
        ip = request.getHeader("HTTP_CLIENT_IP");
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 6. 尝试从HTTP_X_FORWARDED_FOR头获取
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isValidIp(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                ip = ip.substring(0, index).trim();
            }
            return ip;
        }
        
        // 7. 从RemoteAddr获取
        ip = request.getRemoteAddr();
        if (isValidIp(ip)) {
            return ip;
        }
        
        // 8. 如果都获取不到，返回本地IP
        return getLocalIpAddress();
    }
    
    /**
     * 验证IP地址是否有效
     * 
     * @param ip IP地址字符串
     * @return 是否有效
     */
    private static boolean isValidIp(String ip) {
        if (ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip.trim())) {
            return false;
        }
        
        // 检查是否为本地IP
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 获取本机IP地址
     * 
     * @return 本机IP地址
     */
    public static String getLocalIpAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }
    
    /**
     * 获取本机主机名
     * 
     * @return 本机主机名
     */
    public static String getLocalHostName() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }
    
    /**
     * 检查IP地址是否为内网IP
     * 
     * @param ip IP地址
     * @return 是否为内网IP
     */
    public static boolean isInternalIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }
        
        try {
            // 检查是否为本地回环地址
            if (ip.equals("127.0.0.1") || ip.equals("localhost")) {
                return true;
            }
            
            // 检查是否为内网IP段
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            
            int first = Integer.parseInt(parts[0]);
            int second = Integer.parseInt(parts[1]);
            
            // 10.0.0.0 - 10.255.255.255
            if (first == 10) {
                return true;
            }
            
            // 172.16.0.0 - 172.31.255.255
            if (first == 172 && second >= 16 && second <= 31) {
                return true;
            }
            
            // 192.168.0.0 - 192.168.255.255
            if (first == 192 && second == 168) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取IP地址的地理位置信息（简化版）
     * 
     * @param ip IP地址
     * @return 地理位置描述
     */
    public static String getIpLocation(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return "未知";
        }
        
        if (isInternalIp(ip)) {
            return "内网IP";
        }
        
        // 这里可以集成第三方IP地理位置查询服务
        // 例如：淘宝IP库、GeoIP等
        // 暂时返回简单描述
        return "外网IP";
    }
    
    /**
     * 格式化IP地址显示
     * 
     * @param ip IP地址
     * @return 格式化后的IP地址
     */
    public static String formatIpAddress(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return "未知IP";
        }
        
        if (isInternalIp(ip)) {
            return ip + " (内网)";
        }
        
        return ip + " (外网)";
    }
}
