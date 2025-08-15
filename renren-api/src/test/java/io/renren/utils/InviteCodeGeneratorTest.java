package io.renren.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 邀请码生成工具类测试
 *
 * @author renren
 * @since 1.0.0
 */
public class InviteCodeGeneratorTest {

    @BeforeEach
    public void setUp() {
        // 清空已生成的邀请码，确保测试环境干净
        InviteCodeGenerator.clearGeneratedCodes();
    }

    @Test
    public void testGenerateInviteCode() {
        // 测试生成单个邀请码
        String inviteCode = InviteCodeGenerator.generateInviteCode();
        
        assertNotNull(inviteCode, "邀请码不能为空");
        assertEquals(6, inviteCode.length(), "邀请码长度应该是6位");
        assertTrue(InviteCodeGenerator.isValidInviteCode(inviteCode), "生成的邀请码格式应该正确");
        
        System.out.println("生成的邀请码: " + inviteCode);
    }

    @Test
    public void testInviteCodeFormat() {
        // 测试邀请码格式
        String inviteCode = InviteCodeGenerator.generateInviteCode();
        
        // 检查是否只包含大写字母和数字
        assertTrue(inviteCode.matches("^[A-Z0-9]{6}$"), "邀请码应该只包含大写字母和数字");
        
        // 检查是否包含小写字母（不应该包含）
        assertFalse(inviteCode.matches(".*[a-z].*"), "邀请码不应该包含小写字母");
        
        // 检查是否包含特殊字符（不应该包含）
        assertFalse(inviteCode.matches(".*[^A-Z0-9].*"), "邀请码不应该包含特殊字符");
    }

    @Test
    public void testInviteCodeUniqueness() {
        // 测试邀请码唯一性
        Set<String> codes = new HashSet<>();
        int count = 100;
        
        for (int i = 0; i < count; i++) {
            String code = InviteCodeGenerator.generateInviteCode();
            assertFalse(codes.contains(code), "邀请码应该唯一，重复: " + code);
            codes.add(code);
        }
        
        assertEquals(count, codes.size(), "应该生成指定数量的唯一邀请码");
        assertEquals(count, InviteCodeGenerator.getGeneratedCodeCount(), "已生成的邀请码数量应该正确");
    }

    @Test
    public void testGenerateMultipleInviteCodes() {
        // 测试批量生成邀请码
        int count = 10;
        String[] codes = InviteCodeGenerator.generateMultipleInviteCodes(count);
        
        assertEquals(count, codes.length, "应该生成指定数量的邀请码");
        
        // 检查每个邀请码的格式和唯一性
        Set<String> uniqueCodes = new HashSet<>(Arrays.asList(codes));
        assertEquals(count, uniqueCodes.size(), "批量生成的邀请码应该唯一");
        
        for (String code : codes) {
            assertTrue(InviteCodeGenerator.isValidInviteCode(code), "批量生成的邀请码格式应该正确");
        }
        
        System.out.println("批量生成的邀请码: " + Arrays.toString(codes));
    }

    @Test
    public void testValidInviteCodeValidation() {
        // 测试有效的邀请码
        assertTrue(InviteCodeGenerator.isValidInviteCode("ABC123"), "ABC123 应该是有效的邀请码");
        assertTrue(InviteCodeGenerator.isValidInviteCode("123ABC"), "123ABC 应该是有效的邀请码");
        assertTrue(InviteCodeGenerator.isValidInviteCode("ABCDEF"), "ABCDEF 应该是有效的邀请码");
        assertTrue(InviteCodeGenerator.isValidInviteCode("123456"), "123456 应该是有效的邀请码");
    }

    @Test
    public void testInvalidInviteCodeValidation() {
        // 测试无效的邀请码
        assertFalse(InviteCodeGenerator.isValidInviteCode(null), "null 应该是无效的邀请码");
        assertFalse(InviteCodeGenerator.isValidInviteCode(""), "空字符串应该是无效的邀请码");
        assertFalse(InviteCodeGenerator.isValidInviteCode("ABC12"), "5位邀请码应该是无效的");
        assertFalse(InviteCodeGenerator.isValidInviteCode("ABC1234"), "7位邀请码应该是无效的");
        assertFalse(InviteCodeGenerator.isValidInviteCode("abc123"), "包含小写字母的邀请码应该是无效的");
        assertFalse(InviteCodeGenerator.isValidInviteCode("ABC-12"), "包含特殊字符的邀请码应该是无效的");
        assertFalse(InviteCodeGenerator.isValidInviteCode("ABC 12"), "包含空格的邀请码应该是无效的");
    }

    @Test
    public void testEdgeCases() {
        // 测试边界情况
        assertThrows(IllegalArgumentException.class, () -> {
            InviteCodeGenerator.generateMultipleInviteCodes(0);
        }, "生成0个邀请码应该抛出异常");
        
        assertThrows(IllegalArgumentException.class, () -> {
            InviteCodeGenerator.generateMultipleInviteCodes(-1);
        }, "生成负数个邀请码应该抛出异常");
    }

    @Test
    public void testPerformance() {
        // 测试性能（生成1000个邀请码）
        long startTime = System.currentTimeMillis();
        int count = 1000;
        
        String[] codes = InviteCodeGenerator.generateMultipleInviteCodes(count);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertEquals(count, codes.length, "应该生成指定数量的邀请码");
        assertTrue(duration < 5000, "生成1000个邀请码应该在5秒内完成，实际耗时: " + duration + "ms");
        
        System.out.println("生成 " + count + " 个邀请码耗时: " + duration + "ms");
    }

    @Test
    public void testCodeDistribution() {
        // 测试邀请码字符分布（统计字符出现频率）
        int count = 1000;
        String[] codes = InviteCodeGenerator.generateMultipleInviteCodes(count);
        
        // 统计所有字符的出现次数
        int[] charCount = new int[36]; // A-Z(26) + 0-9(10)
        
        for (String code : codes) {
            for (char c : code.toCharArray()) {
                if (c >= 'A' && c <= 'Z') {
                    charCount[c - 'A']++;
                } else if (c >= '0' && c <= '9') {
                    charCount[26 + (c - '0')]++;
                }
            }
        }
        
        // 检查字符分布是否相对均匀（每个字符至少应该出现几次）
        int minExpectedCount = count * 6 / 36 / 2; // 期望最小出现次数
        
        for (int i = 0; i < charCount.length; i++) {
            char c = i < 26 ? (char)('A' + i) : (char)('0' + (i - 26));
            assertTrue(charCount[i] >= minExpectedCount, 
                "字符 " + c + " 出现次数过少: " + charCount[i] + ", 期望至少: " + minExpectedCount);
        }
        
        System.out.println("字符分布统计完成，每个字符至少出现 " + minExpectedCount + " 次");
    }
}
