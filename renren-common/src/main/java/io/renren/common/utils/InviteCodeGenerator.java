package io.renren.common.utils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * 邀请码生成工具类
 * 
 * @author renren
 * @since 1.0.0
 */
public class InviteCodeGenerator {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();
    
    // 用于存储已生成的邀请码，避免重复（在实际应用中应该使用数据库或Redis）
    private static final Set<String> GENERATED_CODES = new HashSet<>();
    
    /**
     * 生成六位大写字母和数字的邀请码
     * 
     * @return 邀请码
     */
    public static String generateInviteCode() {
        String inviteCode;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100; // 最大尝试次数，避免无限循环
        
        do {
            inviteCode = generateRandomCode();
            attempts++;
            
            // 如果尝试次数过多，说明可能出现了问题，重新生成
            if (attempts > MAX_ATTEMPTS) {
                // 清空已生成的代码集合，重新开始
                GENERATED_CODES.clear();
                attempts = 0;
            }
        } while (GENERATED_CODES.contains(inviteCode));
        
        // 将生成的邀请码添加到集合中
        GENERATED_CODES.add(inviteCode);
        
        return inviteCode;
    }
    
    /**
     * 生成随机邀请码
     * 
     * @return 随机邀请码
     */
    private static String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }
        
        return code.toString();
    }
    
    /**
     * 验证邀请码格式是否正确
     * 
     * @param inviteCode 邀请码
     * @return 是否有效
     */
    public static boolean isValidInviteCode(String inviteCode) {
        if (inviteCode == null || inviteCode.length() != CODE_LENGTH) {
            return false;
        }
        
        // 检查是否只包含大写字母和数字
        return inviteCode.matches("^[A-Z0-9]{" + CODE_LENGTH + "}$");
    }
    
    /**
     * 生成指定数量的邀请码
     * 
     * @param count 生成数量
     * @return 邀请码数组
     */
    public static String[] generateMultipleInviteCodes(int count) {
        if (count <= 0) {
            return new String[0];
        }
        
        String[] codes = new String[count];
        for (int i = 0; i < count; i++) {
            codes[i] = generateInviteCode();
        }
        
        return codes;
    }
    
    /**
     * 清空已生成的邀请码集合
     * 注意：这个方法主要用于测试，生产环境不应该调用
     */
    public static void clearGeneratedCodes() {
        GENERATED_CODES.clear();
    }
    
    /**
     * 获取已生成的邀请码数量
     * 
     * @return 邀请码数量
     */
    public static int getGeneratedCodeCount() {
        return GENERATED_CODES.size();
    }
    
    /**
     * 检查邀请码是否已被生成
     * 
     * @param inviteCode 邀请码
     * @return 是否已生成
     */
    public static boolean isCodeGenerated(String inviteCode) {
        return GENERATED_CODES.contains(inviteCode);
    }
}
