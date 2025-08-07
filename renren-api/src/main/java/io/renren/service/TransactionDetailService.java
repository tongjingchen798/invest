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
 import io.renren.dto.TransactionDetailDTO;
 import io.renren.dto.TransactionDetailPageData;
 import io.renren.entity.TransactionDetailEntity;
 
 import java.util.Map;
 
 /**
  * 账变明细表
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 public interface TransactionDetailService extends BaseService<TransactionDetailEntity> {
 
     TransactionDetailPageData<TransactionDetailDTO> queryPageData(Map<String, Object> params);
 }