/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.page.PageData;
import io.renren.common.service.BaseService;
import io.renren.dto.PayInfoDTO;
import io.renren.dto.PayInfoPageData;
import io.renren.entity.PayInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户支付信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public interface PayInfoService extends BaseService<PayInfoEntity> {

    PayInfoPageData<PayInfoDTO> queryPageData(Map<String, Object> params);
    
    /**
     * 批量删除支付信息
     */
    void delete(List<Long> ids);
}