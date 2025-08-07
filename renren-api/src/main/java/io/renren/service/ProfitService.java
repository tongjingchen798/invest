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
 
     ProfitPageData<ProfitDTO> queryPageData(Map<String, Object> params);
 }