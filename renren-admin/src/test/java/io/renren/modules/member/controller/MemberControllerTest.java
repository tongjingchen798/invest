package io.renren.modules.member.controller;

import io.renren.modules.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 会员控制器测试类
 *
 * @author renren
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
public class MemberControllerTest {

    @Resource
    private MemberService memberService;

    @Test
    public void testGetBiaoQianList() {
        try {
            // 测试获取标签列表
            List<String> tagList = memberService.getBiaoQianList();
            
            // 验证结果不为空
            assertNotNull(tagList, "标签列表不能为空");
            
            System.out.println("标签列表获取成功！");
            System.out.println("标签数量: " + tagList.size());
            
            // 打印所有标签
            if (!tagList.isEmpty()) {
                System.out.println("标签列表:");
                for (String tag : tagList) {
                    System.out.println("  - " + tag);
                }
            } else {
                System.out.println("暂无标签数据");
            }
            
        } catch (Exception e) {
            System.err.println("测试失败: " + e.getMessage());
            e.printStackTrace();
            fail("测试应该成功");
        }
    }
}
