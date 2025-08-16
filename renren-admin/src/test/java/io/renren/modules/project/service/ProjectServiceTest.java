package io.renren.modules.project.service;

import io.renren.common.page.PageData;
import io.renren.modules.project.entity.ProjectEntity;
import io.renren.modules.project.service.impl.ProjectServiceImpl;
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
    public void testGetProjectPageWithConditions() {
        // 准备带条件的测试参数
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 5);
        params.put("status", "1"); // 只查询上架的项目
        params.put("orderField", "create_date");
        params.put("order", "desc");
        
        try {
            // 执行分页查询
            PageData<ProjectEntity> result = projectService.getProjectPage(params);
            
            // 验证结果
            assertNotNull(result, "分页结果不能为空");
            assertNotNull(result.getList(), "分页列表不能为空");
            
            System.out.println("条件分页查询成功！");
            System.out.println("总记录数: " + result.getTotal());
            System.out.println("当前页记录数: " + result.getList().size());
            
            // 验证状态筛选
            for (ProjectEntity project : result.getList()) {
                assertEquals(1, project.getStatus(), "所有项目状态应该是1（上架）");
            }
            
        } catch (Exception e) {
            fail("条件分页查询失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetProjectPageWithNameSearch() {
        // 准备名称搜索的测试参数
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 10);
        params.put("investName", "Happy"); // 搜索包含"Happy"的项目
        
        try {
            // 执行分页查询
            PageData<ProjectEntity> result = projectService.getProjectPage(params);
            
            // 验证结果
            assertNotNull(result, "分页结果不能为空");
            assertNotNull(result.getList(), "分页列表不能为空");
            
            System.out.println("名称搜索分页查询成功！");
            System.out.println("总记录数: " + result.getTotal());
            System.out.println("当前页记录数: " + result.getList().size());
            
            // 验证名称筛选
            for (ProjectEntity project : result.getList()) {
                assertTrue(project.getInvestName().contains("Happy"), 
                    "项目名称应该包含'Happy'");
            }
            
        } catch (Exception e) {
            fail("名称搜索分页查询失败: " + e.getMessage());
        }
    }

    @Test
    public void testGetProjectPagePagination() {
        // 测试分页功能
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 2); // 每页2条
        
        try {
            // 第一页
            PageData<ProjectEntity> page1 = projectService.getProjectPage(params);
            assertNotNull(page1, "第一页结果不能为空");
            assertTrue(page1.getList().size() <= 2, "第一页记录数不能超过2");
            
            // 第二页
            params.put("page", 2);
            PageData<ProjectEntity> page2 = projectService.getProjectPage(params);
            assertNotNull(page2, "第二页结果不能为空");
            
            System.out.println("分页功能测试成功！");
            System.out.println("第一页记录数: " + page1.getList().size());
            System.out.println("第二页记录数: " + page2.getList().size());
            System.out.println("总记录数: " + page1.getTotal());
            
        } catch (Exception e) {
            fail("分页功能测试失败: " + e.getMessage());
        }
    }
}
