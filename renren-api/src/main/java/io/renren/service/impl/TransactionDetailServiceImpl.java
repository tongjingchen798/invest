/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.dao.TransactionDetailDao;
import io.renren.dto.TransactionDetailDTO;
import io.renren.dto.TransactionDetailPageData;
import io.renren.entity.TransactionDetailEntity;
import io.renren.service.TransactionDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 账变明细表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Service
public class TransactionDetailServiceImpl extends BaseServiceImpl<TransactionDetailDao, TransactionDetailEntity> implements TransactionDetailService {

    @Override
    public TransactionDetailPageData<TransactionDetailDTO> queryPageData(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<TransactionDetailEntity> queryWrapper = buildQueryWrapper(params);
        
        // 使用 BaseServiceImpl 的标准分页处理
        IPage<TransactionDetailEntity> pageResult = baseDao.selectPage(
            getPage(params, "create_time", false),
            queryWrapper
        );
        
        // 转换为DTO
        List<TransactionDetailDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new TransactionDetailPageData<>(dtoList, (int) pageResult.getTotal());
    }
    
    @Override
    public TransactionDetailPageData<TransactionDetailDTO> queryPageData(Long userId, Integer page, Integer limit) {
        // 创建MyBatis-Plus分页对象
        Page<TransactionDetailEntity> pageParam = new Page<>(page, limit);
        
        // 构建查询条件
        QueryWrapper<TransactionDetailEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("transaction_date", "create_time");
        
        // 执行分页查询
        IPage<TransactionDetailEntity> pageResult = baseDao.selectPage(pageParam, queryWrapper);
        
        // 转换为DTO
        List<TransactionDetailDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new TransactionDetailPageData<>(dtoList, (int) pageResult.getTotal());
    }

    /**
     * 转换为DTO
     */
    public List<TransactionDetailDTO> convertToDto(List<TransactionDetailEntity> entityList) {
        return ConvertUtils.sourceToTarget(entityList, TransactionDetailDTO.class);
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<TransactionDetailEntity> buildQueryWrapper(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String mobile = (String) params.get("mobile");
        Integer busiType = (Integer) params.get("busiType");
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        QueryWrapper<TransactionDetailEntity> queryWrapper = new QueryWrapper<TransactionDetailEntity>();
        
        // 用户ID筛选
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        
        // 手机号筛选
        if (StringUtils.isNotBlank(mobile)) {
            queryWrapper.eq("mobile", mobile);
        }
        
        // 业务类型筛选
        if (busiType != null) {
            queryWrapper.eq("busi_type", busiType);
        }
        
        // 时间范围筛选
        if (StringUtils.isNotBlank(startDate)) {
            queryWrapper.ge("transaction_date", startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            queryWrapper.le("transaction_date", endDate);
        }
        
        // 默认按交易时间倒序排序
        queryWrapper.orderByDesc("transaction_date");

        return queryWrapper;
    }
}