
package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.dao.ProfitDao;
import io.renren.dto.ProfitDTO;
import io.renren.dto.ProfitPageData;
import io.renren.entity.ProfitEntity;
import io.renren.service.ProfitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 付息还本表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Service
public class ProfitServiceImpl extends BaseServiceImpl<ProfitDao, ProfitEntity> implements ProfitService {

    @Override
    public ProfitPageData<ProfitDTO> queryPageData(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<ProfitEntity> queryWrapper = buildQueryWrapper(params);
        
        IPage<ProfitEntity> pageResult = baseDao.selectPage(
            getPage(params, "create_time", false),
            queryWrapper
        );
        
        // 转换为DTO
        List<ProfitDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new ProfitPageData<>(dtoList, (int) pageResult.getTotal());
    }
    
    @Override
    public ProfitPageData<ProfitDTO> queryPageData(Long userId, Integer page, Integer limit) {
        // 创建MyBatis-Plus分页对象
        Page<ProfitEntity> pageParam = new Page<>(page, limit);
        
        // 构建查询条件
        QueryWrapper<ProfitEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .orderByDesc("create_time");
        
        // 执行分页查询
        IPage<ProfitEntity> pageResult = baseDao.selectPage(pageParam, queryWrapper);
        
        // 转换为DTO
        List<ProfitDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new ProfitPageData<>(dtoList, (int) pageResult.getTotal());
    }

    /**
     * 转换为DTO
     */
    public List<ProfitDTO> convertToDto(List<ProfitEntity> entityList) {
        return ConvertUtils.sourceToTarget(entityList, ProfitDTO.class);
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<ProfitEntity> buildQueryWrapper(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Integer status = (Integer) params.get("status");

        QueryWrapper<ProfitEntity> queryWrapper = new QueryWrapper<ProfitEntity>();
        
        // 用户ID筛选
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        
        // 状态筛选
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 默认按创建时间倒序排序
        queryWrapper.orderByDesc("create_time");

        return queryWrapper;
    }
}