package io.renren.modules.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.page.PageData;
import io.renren.modules.project.entity.ProjectEntity;

import java.util.Map;

/**
 * 投资项目服务接口
 * 
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface ProjectService extends IService<ProjectEntity> {
    
    /**
     * 分页查询项目
     * @param params 查询参数
     * @return 分页数据
     */
    PageData<ProjectEntity> getProjectPage(Map<String, Object> params);
}
