/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.service.BaseService;
import io.renren.entity.ProjectEntity;
import io.renren.dto.ProjectDTO;

import java.util.List;

/**
 * 投资项目
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public interface ProjectService extends BaseService<ProjectEntity> {

    /**
     * 查询投资项目列表（分组）
     */
    List<ProjectDTO> queryListGroup();
}
