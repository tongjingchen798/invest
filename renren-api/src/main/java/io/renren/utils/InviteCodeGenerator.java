package io.renren.utils;

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
    
}
