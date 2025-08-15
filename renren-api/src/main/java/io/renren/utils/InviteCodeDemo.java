package io.renren.utils;

/**
 * 邀请码生成演示类
 * 
 * 展示如何使用InviteCodeGenerator生成邀请码
 * 
 * @author renren
 * @since 1.0.0
 */
public class InviteCodeDemo {
    
    public static void main(String[] args) {
        System.out.println("=== 邀请码生成演示 ===\n");
        
        // 1. 生成单个邀请码
        System.out.println("1. 生成单个邀请码:");
        String inviteCode = InviteCodeGenerator.generateInviteCode();
        System.out.println("   生成的邀请码: " + inviteCode);
        System.out.println("   邀请码长度: " + inviteCode.length());
        System.out.println("   格式验证: " + (InviteCodeGenerator.isValidInviteCode(inviteCode) ? "有效" : "无效"));
        System.out.println();
        
        // 2. 批量生成邀请码
        System.out.println("2. 批量生成邀请码:");
        String[] multipleCodes = InviteCodeGenerator.generateMultipleInviteCodes(5);
        System.out.println("   生成的邀请码:");
        for (int i = 0; i < multipleCodes.length; i++) {
            System.out.println("   " + (i + 1) + ". " + multipleCodes[i]);
        }
        System.out.println();
        
        // 3. 验证邀请码格式
        System.out.println("3. 邀请码格式验证:");
        String[] testCodes = {"ABC123", "abc123", "ABC-12", "ABC 12", "ABC12", "ABC1234", "", null};
        for (String testCode : testCodes) {
            String displayCode = testCode == null ? "null" : "'" + testCode + "'";
            String result = InviteCodeGenerator.isValidInviteCode(testCode) ? "有效" : "无效";
            System.out.println("   " + displayCode + " -> " + result);
        }
        System.out.println();
        
        // 4. 统计信息
        System.out.println("4. 统计信息:");
        System.out.println("   已生成的邀请码数量: " + InviteCodeGenerator.getGeneratedCodeCount());
        System.out.println();
        
        // 5. 生成更多邀请码并检查唯一性
        System.out.println("5. 唯一性测试:");
        int testCount = 100;
        String[] uniqueTestCodes = InviteCodeGenerator.generateMultipleInviteCodes(testCount);
        java.util.Set<String> uniqueSet = new java.util.HashSet<>();
        for (String code : uniqueTestCodes) {
            uniqueSet.add(code);
        }
        System.out.println("   生成 " + testCount + " 个邀请码");
        System.out.println("   唯一邀请码数量: " + uniqueSet.size());
        System.out.println("   重复检查: " + (uniqueSet.size() == testCount ? "无重复" : "有重复"));
        System.out.println();
        
        // 6. 性能测试
        System.out.println("6. 性能测试:");
        long startTime = System.currentTimeMillis();
        InviteCodeGenerator.generateMultipleInviteCodes(1000);
        long endTime = System.currentTimeMillis();
        System.out.println("   生成1000个邀请码耗时: " + (endTime - startTime) + "ms");
        System.out.println();
        
        System.out.println("=== 演示完成 ===");
    }
}
