package io.renren.service;

import io.renren.common.page.PageData;
import io.renren.entity.ProjectEntity;
import io.renren.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 项目服务测试类
 * 
 * @author renren
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
public class ProjectServiceTest {

    @Resource
    private ProjectService projectService;

    @Test
    public void testGetProjectPage() {
        // 准备测试参数
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 10);

        try {
            // 执行分页查询
            PageData<ProjectEntity> result = projectService.getProjectPage(params);

            // 验证结果
            assertNotNull(result, "分页结果不能为空");
            assertNotNull(result.getList(), "分页列表不能为空");
            assertTrue(result.getTotal() >= 0, "总记录数必须大于等于0");

            System.out.println("分页查询成功！");
            System.out.println("总记录数: " + result.getTotal());
            System.out.println("当前页记录数: " + result.getList().size());

            // 如果有数据，打印第一条记录
            if (!result.getList().isEmpty()) {
                ProjectEntity firstProject = result.getList().get(0);
                System.out.println("第一条项目: " + firstProject.getInvestName());
            }

        } catch (Exception e) {
            fail("分页查询失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetProjectPageWithIntegerParams() {
        // 测试使用Integer类型的参数
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 5);
        params.put("status", 1); // Integer类型
        params.put("projectType", 1); // Integer类型
        params.put("cycleType", 2); // Integer类型

        try {
            // 执行分页查询
            PageData<ProjectEntity> result = projectService.getProjectPage(params);

            // 验证结果
            assertNotNull(result, "分页结果不能为空");
            assertNotNull(result.getList(), "分页列表不能为空");

            System.out.println("Integer参数分页查询成功！");
            System.out.println("总记录数: " + result.getTotal());
            System.out.println("当前页记录数: " + result.getList().size());

        } catch (Exception e) {
            fail("Integer参数分页查询失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetProjectPageWithStringParams() {
        // 测试使用String类型的参数
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 5);
        params.put("status", "1"); // String类型
        params.put("projectType", "1"); // String类型
        params.put("cycleType", "2"); // String类型

        try {
            // 执行分页查询
            PageData<ProjectEntity> result = projectService.getProjectPage(params);

            // 验证结果
            assertNotNull(result, "分页结果不能为空");
            assertNotNull(result.getList(), "分页列表不能为空");

            System.out.println("String参数分页查询成功！");
            System.out.println("总记录数: " + result.getTotal());
            System.out.println("当前页记录数: " + result.getList().size());

        } catch (Exception e) {
            fail("String参数分页查询失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetProjectPageWithMixedParams() {
        // 测试混合类型的参数
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 5);
        params.put("status", 1); // Integer类型
        params.put("projectType", "2"); // String类型
        params.put("investName", "测试"); // String类型

        try {
            // 执行分页查询
            PageData<ProjectEntity> result = projectService.getProjectPage(params);

            // 验证结果
            assertNotNull(result, "分页结果不能为空");
            assertNotNull(result.getList(), "分页列表不能为空");

            System.out.println("混合参数分页查询成功！");
            System.out.println("总记录数: " + result.getTotal());
            System.out.println("当前页记录数: " + result.getList().size());

        } catch (Exception e) {
            fail("混合参数分页查询失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetProjectPageWithInvalidParams() {
        // 测试无效的参数值
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 5);
        params.put("status", "invalid"); // 无效的状态值
        params.put("projectType", "abc"); // 无效的项目类型值

        try {
            // 执行分页查询，应该不会抛出异常
            PageData<ProjectEntity> result = projectService.getProjectPage(params);

            // 验证结果
            assertNotNull(result, "分页结果不能为空");
            assertNotNull(result.getList(), "分页列表不能为空");

            System.out.println("无效参数分页查询成功！");
            System.out.println("总记录数: " + result.getTotal());
            System.out.println("当前页记录数: " + result.getList().size());

        } catch (Exception e) {
            fail("无效参数分页查询失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetProjectPageWithNullParams() {
        // 测试空参数
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 5);
        params.put("status", null); // null值
        params.put("projectType", null); // null值

        try {
            // 执行分页查询
            PageData<ProjectEntity> result = projectService.getProjectPage(params);

            // 验证结果
            assertNotNull(result, "分页结果不能为空");
            assertNotNull(result.getList(), "分页列表不能为空");

            System.out.println("空参数分页查询成功！");
            System.out.println("总记录数: " + result.getTotal());
            System.out.println("当前页记录数: " + result.getList().size());

        } catch (Exception e) {
            fail("空参数分页查询失败: " + e.getMessage());
        }
    }
}
