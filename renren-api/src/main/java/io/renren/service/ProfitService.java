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
 import io.renren.dto.ProfitDTO;
 import io.renren.dto.ProfitPageData;
 import io.renren.entity.ProfitEntity;
 
 import java.util.Map;
 
 /**
  * 付息还本表
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 public interface ProfitService extends BaseService<ProfitEntity> {
 
     /**
      * 查询分页数据（使用Map参数）
      */
     ProfitPageData<ProfitDTO> queryPageData(Map<String, Object> params);
     
     /**
      * 查询分页数据（直接参数）
      * @param userId 用户ID
      * @param page 页码
      * @param limit 每页大小
      * @return 分页数据
      */
     ProfitPageData<ProfitDTO> queryPageData(Long userId, Integer page, Integer limit);
 }