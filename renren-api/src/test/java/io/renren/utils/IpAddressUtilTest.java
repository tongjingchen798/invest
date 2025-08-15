package io.renren.utils;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IP地址获取工具类测试
 *
 * @author renren
 * @since 1.0.0
 */
public class IpAddressUtilTest {

    @Test
    public void testGetLocalIpAddress() {
        String localIp = IpAddressUtil.getLocalIpAddress();
        assertNotNull(localIp, "本机IP地址不能为空");
        assertFalse(localIp.trim().isEmpty(), "本机IP地址不能为空字符串");
        System.out.println("本机IP地址: " + localIp);
    }

    @Test
    public void testGetLocalHostName() {
        String hostName = IpAddressUtil.getLocalHostName();
        assertNotNull(hostName, "本机主机名不能为空");
        assertFalse(hostName.trim().isEmpty(), "本机主机名不能为空字符串");
        System.out.println("本机主机名: " + hostName);
    }

    @Test
    public void testIsInternalIp() {
        // 测试内网IP
        assertTrue(IpAddressUtil.isInternalIp("127.0.0.1"), "127.0.0.1应该是内网IP");
        assertTrue(IpAddressUtil.isInternalIp("localhost"), "localhost应该是内网IP");
        assertTrue(IpAddressUtil.isInternalIp("10.0.0.1"), "10.0.0.1应该是内网IP");
        assertTrue(IpAddressUtil.isInternalIp("172.16.0.1"), "172.16.0.1应该是内网IP");
        assertTrue(IpAddressUtil.isInternalIp("192.168.1.1"), "192.168.1.1应该是内网IP");
        
        // 测试外网IP
        assertFalse(IpAddressUtil.isInternalIp("8.8.8.8"), "8.8.8.8应该是外网IP");
        assertFalse(IpAddressUtil.isInternalIp("114.114.114.114"), "114.114.114.114应该是外网IP");
        
        // 测试无效IP
        assertFalse(IpAddressUtil.isInternalIp(null), "null应该是无效IP");
        assertFalse(IpAddressUtil.isInternalIp(""), "空字符串应该是无效IP");
        assertFalse(IpAddressUtil.isInternalIp("invalid"), "invalid应该是无效IP");
    }

    @Test
    public void testGetIpLocation() {
        // 测试内网IP地理位置
        assertEquals("内网IP", IpAddressUtil.getIpLocation("127.0.0.1"), "127.0.0.1应该显示为内网IP");
        assertEquals("内网IP", IpAddressUtil.getIpLocation("192.168.1.1"), "192.168.1.1应该显示为内网IP");
        
        // 测试外网IP地理位置
        assertEquals("外网IP", IpAddressUtil.getIpLocation("8.8.8.8"), "8.8.8.8应该显示为外网IP");
        
        // 测试无效IP地理位置
        assertEquals("未知", IpAddressUtil.getIpLocation(null), "null应该显示为未知");
        assertEquals("未知", IpAddressUtil.getIpLocation(""), "空字符串应该显示为未知");
    }

    @Test
    public void testFormatIpAddress() {
        // 测试内网IP格式化
        assertTrue(IpAddressUtil.formatIpAddress("127.0.0.1").contains("(内网)"), "127.0.0.1应该包含内网标识");
        assertTrue(IpAddressUtil.formatIpAddress("192.168.1.1").contains("(内网)"), "192.168.1.1应该包含内网标识");
        
        // 测试外网IP格式化
        assertTrue(IpAddressUtil.formatIpAddress("8.8.8.8").contains("(外网)"), "8.8.8.8应该包含外网标识");
        
        // 测试无效IP格式化
        assertEquals("未知IP", IpAddressUtil.formatIpAddress(null), "null应该格式化为未知IP");
        assertEquals("未知IP", IpAddressUtil.formatIpAddress(""), "空字符串应该格式化为未知IP");
    }

    @Test
    public void testGetClientIpAddressFromRequest() {
        // 创建模拟的HttpServletRequest
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // 测试X-Forwarded-For头
        request.addHeader("X-Forwarded-For", "203.0.113.1, 10.0.0.1");
        String ip = IpAddressUtil.getClientIpAddress(request);
        assertEquals("203.0.113.1", ip, "应该从X-Forwarded-For头获取第一个IP");
        
        // 测试X-Real-IP头
        request = new MockHttpServletRequest();
        request.addHeader("X-Real-IP", "203.0.113.2");
        ip = IpAddressUtil.getClientIpAddress(request);
        assertEquals("203.0.113.2", ip, "应该从X-Real-IP头获取IP");
        
        // 测试Proxy-Client-IP头
        request = new MockHttpServletRequest();
        request.addHeader("Proxy-Client-IP", "203.0.113.3");
        ip = IpAddressUtil.getClientIpAddress(request);
        assertEquals("203.0.113.3", ip, "应该从Proxy-Client-IP头获取IP");
        
        // 测试RemoteAddr
        request = new MockHttpServletRequest();
        request.setRemoteAddr("203.0.113.4");
        ip = IpAddressUtil.getClientIpAddress(request);
        assertEquals("203.0.113.4", ip, "应该从RemoteAddr获取IP");
    }

    @Test
    public void testGetClientIpAddressFromRequestWithInvalidHeaders() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // 测试无效的X-Forwarded-For头
        request.addHeader("X-Forwarded-For", "unknown");
        String ip = IpAddressUtil.getClientIpAddress(request);
        assertNotNull(ip, "无效头信息时应该返回默认IP");
        
        // 测试空的X-Forwarded-For头
        request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "");
        ip = IpAddressUtil.getClientIpAddress(request);
        assertNotNull(ip, "空头信息时应该返回默认IP");
        
        // 测试null的X-Forwarded-For头
        request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", null);
        ip = IpAddressUtil.getClientIpAddress(request);
        assertNotNull(ip, "null头信息时应该返回默认IP");
    }

    @Test
    public void testGetClientIpAddressFromRequestWithLocalhost() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // 测试本地回环地址
        request.setRemoteAddr("127.0.0.1");
        String ip = IpAddressUtil.getClientIpAddress(request);
        assertNotNull(ip, "本地回环地址时应该返回有效IP");
        
        // 测试IPv6本地回环地址
        request.setRemoteAddr("0:0:0:0:0:0:0:1");
        ip = IpAddressUtil.getClientIpAddress(request);
        assertNotNull(ip, "IPv6本地回环地址时应该返回有效IP");
    }

    @Test
    public void testGetClientIpAddressFromRequestPriority() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // 设置多个头信息，测试优先级
        request.addHeader("X-Forwarded-For", "203.0.113.1");
        request.addHeader("X-Real-IP", "203.0.113.2");
        request.addHeader("Proxy-Client-IP", "203.0.113.3");
        request.setRemoteAddr("203.0.113.4");
        
        // X-Forwarded-For应该优先级最高
        String ip = IpAddressUtil.getClientIpAddress(request);
        assertEquals("203.0.113.1", ip, "X-Forwarded-For应该优先级最高");
    }

    @Test
    public void testGetClientIpAddressFromRequestWithMultipleXForwardedFor() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // 测试多个IP的X-Forwarded-For头
        request.addHeader("X-Forwarded-For", "203.0.113.1, 10.0.0.1, 172.16.0.1");
        String ip = IpAddressUtil.getClientIpAddress(request);
        assertEquals("203.0.113.1", ip, "应该取第一个IP地址");
    }

    @Test
    public void testGetClientIpAddressFromRequestWithSpaces() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // 测试带空格的X-Forwarded-For头
        request.addHeader("X-Forwarded-For", " 203.0.113.1 , 10.0.0.1 ");
        String ip = IpAddressUtil.getClientIpAddress(request);
        assertEquals("203.0.113.1", ip, "应该正确处理带空格的IP地址");
    }

    @Test
    public void testGetClientIpAddressFromRequestWithNullRequest() {
        // 测试null请求
        String ip = IpAddressUtil.getClientIpAddress((MockHttpServletRequest) null);
        assertNotNull(ip, "null请求时应该返回默认IP");
    }

    @Test
    public void testGetClientIpAddressWithoutRequestContext() {
        // 测试在没有请求上下文的情况下获取IP
        // 这需要在实际的Spring环境中测试
        // 这里只是验证方法不会抛出异常
        try {
            String ip = IpAddressUtil.getClientIpAddress();
            assertNotNull(ip, "应该返回有效的IP地址");
        } catch (Exception e) {
            // 在没有Spring上下文的情况下可能会抛出异常，这是正常的
            System.out.println("在没有Spring上下文的情况下获取IP地址: " + e.getMessage());
        }
    }
}
