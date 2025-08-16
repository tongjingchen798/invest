package io.renren.controller;

import io.renren.dto.IssuesDTO;
import io.renren.dto.IssuesPageData;
import io.renren.service.IssuesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 广告API控制器测试类 - 使用MyBatis-Plus分页
 * 
 * @author renren
 * @since 1.0.0
 */
@WebMvcTest(ApiAdvertisementController.class)
public class ApiAdvertisementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssuesService issuesService;

    @Test
    public void testGetAdvertisementPage() throws Exception {
        // 准备测试数据
        List<IssuesDTO> advertisementList = new ArrayList<>();
        IssuesDTO advertisement = new IssuesDTO();
        advertisement.setId(1L);
        advertisement.setTitle("首页Logo");
        advertisement.setContent("网站Logo图片");
        advertisement.setImagesAddr("https://example.com/logo.png");
        advertisement.setType(1);
        advertisement.setSort(1);
        advertisement.setStatus(1);
        advertisement.setCreateDate(new Date());
        advertisementList.add(advertisement);

        IssuesPageData<IssuesDTO> pageData = new IssuesPageData<>(advertisementList, 1);

        // Mock服务层返回
        when(issuesService.queryPageData(any(Map.class))).thenReturn(pageData);

        // 执行测试
        mockMvc.perform(get("/api/advertisement/page")
                .param("page", "1")
                .param("limit", "10")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.list[0].id").value(1))
                .andExpect(jsonPath("$.data.list[0].title").value("首页Logo"))
                .andExpect(jsonPath("$.data.list[0].content").value("网站Logo图片"))
                .andExpect(jsonPath("$.data.list[0].imagesAddr").value("https://example.com/logo.png"))
                .andExpect(jsonPath("$.data.list[0].type").value(1))
                .andExpect(jsonPath("$.data.list[0].sort").value(1))
                .andExpect(jsonPath("$.data.list[0].status").value(1));
    }

    @Test
    public void testGetAdvertisementPageWithType() throws Exception {
        // 准备测试数据
        List<IssuesDTO> advertisementList = new ArrayList<>();
        IssuesDTO advertisement = new IssuesDTO();
        advertisement.setId(2L);
        advertisement.setTitle("轮播图1");
        advertisement.setContent("首页轮播图");
        advertisement.setImagesAddr("https://example.com/banner1.jpg");
        advertisement.setType(2);
        advertisement.setSort(1);
        advertisement.setStatus(1);
        advertisement.setCreateDate(new Date());
        advertisementList.add(advertisement);

        IssuesPageData<IssuesDTO> pageData = new IssuesPageData<>(advertisementList, 1);

        // Mock服务层返回
        when(issuesService.queryPageData(any(Map.class))).thenReturn(pageData);

        // 执行测试
        mockMvc.perform(get("/api/advertisement/page")
                .param("page", "1")
                .param("limit", "10")
                .param("type", "2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.list[0].id").value(2))
                .andExpect(jsonPath("$.data.list[0].title").value("轮播图1"))
                .andExpect(jsonPath("$.data.list[0].type").value(2));
    }

    @Test
    public void testGetAdvertisementPageWithError() throws Exception {
        // Mock服务层抛出异常
        when(issuesService.queryPageData(any(Map.class)))
                .thenThrow(new RuntimeException("数据库连接失败"));

        // 执行测试
        mockMvc.perform(get("/api/advertisement/page")
                .param("page", "1")
                .param("limit", "10")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value("查询失败：数据库连接失败"));
    }

    @Test
    public void testGetAdvertisementPageWithEmptyResult() throws Exception {
        // 准备空结果数据
        IssuesPageData<IssuesDTO> pageData = new IssuesPageData<>(new ArrayList<>(), 0);

        // Mock服务层返回
        when(issuesService.queryPageData(any(Map.class))).thenReturn(pageData);

        // 执行测试
        mockMvc.perform(get("/api/advertisement/page")
                .param("page", "1")
                .param("limit", "10")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.list").isEmpty());
    }

    @Test
    public void testGetAdvertisementPageWithDefaultParams() throws Exception {
        // 准备测试数据
        List<IssuesDTO> advertisementList = new ArrayList<>();
        IssuesDTO advertisement = new IssuesDTO();
        advertisement.setId(3L);
        advertisement.setTitle("个人中心图片");
        advertisement.setContent("个人中心背景图");
        advertisement.setImagesAddr("https://example.com/profile.jpg");
        advertisement.setType(3);
        advertisement.setSort(1);
        advertisement.setStatus(1);
        advertisement.setCreateDate(new Date());
        advertisementList.add(advertisement);

        IssuesPageData<IssuesDTO> pageData = new IssuesPageData<>(advertisementList, 1);

        // Mock服务层返回
        when(issuesService.queryPageData(any(Map.class))).thenReturn(pageData);

        // 执行测试 - 不传递分页参数，应该使用默认值
        mockMvc.perform(get("/api/advertisement/page")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.list").isArray())
                .andExpect(jsonPath("$.data.list[0].id").value(3))
                .andExpect(jsonPath("$.data.list[0].title").value("个人中心图片"))
                .andExpect(jsonPath("$.data.list[0].type").value(3));
    }

    @Test
    public void testListByType() throws Exception {
        // 准备测试数据
        List<IssuesDTO> advertisementList = new ArrayList<>();
        IssuesDTO advertisement = new IssuesDTO();
        advertisement.setId(4L);
        advertisement.setTitle("弹窗广告");
        advertisement.setContent("弹窗形式的广告");
        advertisement.setImagesAddr("https://example.com/popup.jpg");
        advertisement.setType(4);
        advertisement.setSort(1);
        advertisement.setStatus(1);
        advertisement.setCreateDate(new Date());
        advertisementList.add(advertisement);

        IssuesPageData<IssuesDTO> pageData = new IssuesPageData<>(advertisementList, 1);

        // Mock服务层返回
        when(issuesService.queryPageData(any(Map.class))).thenReturn(pageData);

        // 执行测试
        mockMvc.perform(get("/api/advertisement/listByType")
                .param("type", "4")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").hasSize(1))
                .andExpect(jsonPath("$.data[0].id").value(4))
                .andExpect(jsonPath("$.data[0].title").value("弹窗广告"))
                .andExpect(jsonPath("$.data[0].type").value(4));
    }

    @Test
    public void testListEnabled() throws Exception {
        // 准备测试数据
        List<IssuesDTO> advertisementList = new ArrayList<>();
        
        IssuesDTO advertisement1 = new IssuesDTO();
        advertisement1.setId(1L);
        advertisement1.setTitle("首页Logo");
        advertisement1.setType(1);
        advertisement1.setStatus(1);
        advertisementList.add(advertisement1);

        IssuesDTO advertisement2 = new IssuesDTO();
        advertisement2.setId(2L);
        advertisement2.setTitle("轮播图1");
        advertisement2.setType(2);
        advertisement2.setStatus(1);
        advertisementList.add(advertisement2);

        IssuesPageData<IssuesDTO> pageData = new IssuesPageData<>(advertisementList, 2);

        // Mock服务层返回
        when(issuesService.queryPageData(any(Map.class))).thenReturn(pageData);

        // 执行测试
        mockMvc.perform(get("/api/advertisement/listEnabled")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").hasSize(2))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("首页Logo"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].title").value("轮播图1"));
    }
}
