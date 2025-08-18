package io.renren.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.dao.AdvertisementDao;
import io.renren.entity.AdvertisementEntity;
import io.renren.dto.AdvertisementDTO;
import io.renren.common.page.PageData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 广告服务分页功能测试类
 * 测试MyBatis-Plus分页插件的功能
 *
 * @author renren
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("test")
public class AdvertisementServicePageTest {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private AdvertisementDao advertisementDao;

    @Test
    public void testMybatisPlusPagination() {
        // 测试MyBatis-Plus分页插件的分页查询
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 5);

        PageData<AdvertisementDTO> result = advertisementService.getPage(params);

        // 验证分页结果
        assertNotNull(result);
        assertNotNull(result.getList());
        assertTrue(result.getTotal() >= 0);
        assertTrue(result.getList().size() <= 5); // 每页最多5条记录
    }

    @Test
    public void testPaginationWithTypeFilter() {
        // 测试带类型筛选的分页查询
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 10);
        params.put("type", 1); // 只查询LOG类型的广告

        PageData<AdvertisementDTO> result = advertisementService.getPage(params);

        // 验证分页结果
        assertNotNull(result);
        assertNotNull(result.getList());
        
        // 验证所有返回的记录都是指定类型
        for (AdvertisementDTO dto : result.getList()) {
            assertEquals(1, dto.getType());
        }
    }

    @Test
    public void testPaginationWithOrdering() {
        // 测试带排序的分页查询
        Map<String, Object> params = new HashMap<>();
        params.put("page", 1);
        params.put("limit", 10);
        params.put("orderField", "create_date");
        params.put("order", "desc");

        PageData<AdvertisementDTO> result = advertisementService.getPage(params);

        // 验证分页结果
        assertNotNull(result);
        assertNotNull(result.getList());
        assertTrue(result.getList().size() > 0);
    }

    @Test
    public void testPaginationDefaultValues() {
        // 测试不传递分页参数时的默认值
        Map<String, Object> params = new HashMap<>();
        // 不设置page和limit，应该使用默认值

        PageData<AdvertisementDTO> result = advertisementService.getPage(params);

        // 验证分页结果
        assertNotNull(result);
        assertNotNull(result.getList());
        // 默认应该返回第一页，每页10条记录
        assertTrue(result.getList().size() <= 10);
    }

    @Test
    public void testPaginationSecondPage() {
        // 测试第二页分页
        Map<String, Object> params = new HashMap<>();
        params.put("page", 2);
        params.put("limit", 3);

        PageData<AdvertisementDTO> result = advertisementService.getPage(params);

        // 验证分页结果
        assertNotNull(result);
        assertNotNull(result.getList());
        // 第二页的记录数应该小于等于每页限制
        assertTrue(result.getList().size() <= 3);
    }

    @Test
    public void testMybatisPlusPageObject() {
        // 直接测试MyBatis-Plus的Page对象
        Page<AdvertisementEntity> page = new Page<>(1, 5);
        
        QueryWrapper<AdvertisementEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        IPage<AdvertisementEntity> result = advertisementDao.selectPage(page, queryWrapper);
        
        // 验证MyBatis-Plus分页结果
        assertNotNull(result);
        assertNotNull(result.getRecords());
        assertTrue(result.getTotal() >= 0);
        assertTrue(result.getCurrent() == 1);
        assertTrue(result.getSize() == 5);
        assertTrue(result.getRecords().size() <= 5);
    }
}
