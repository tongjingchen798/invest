

package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.dao.BankDao;
import io.renren.dto.BankDTO;
import io.renren.dto.BankPageData;
import io.renren.entity.BankEntity;
import io.renren.service.BankService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 银行管理
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Service
public class BankServiceImpl extends BaseServiceImpl<BankDao, BankEntity> implements BankService {

    @Override
    public BankPageData<BankDTO> queryPageData(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<BankEntity> queryWrapper = buildQueryWrapper(params);
        
        IPage<BankEntity> pageResult = baseDao.selectPage(
            getPage(params, "create_time", false),
            queryWrapper
        );
        
        // 转换为DTO
        List<BankDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new BankPageData<>(dtoList, (int) pageResult.getTotal());
    }
    
    @Override
    public BankPageData<BankDTO> queryPageData(Integer page, Integer limit, String order, String orderField, Integer state) {
        // 创建MyBatis-Plus分页对象
        Page<BankEntity> pageParam = new Page<>(page, limit);
        
        // 构建查询条件
        QueryWrapper<BankEntity> queryWrapper = new QueryWrapper<>();
        
        // 状态筛选
        if (state != null) {
            queryWrapper.eq("state", state);
        }
        
        // 排序处理
        if (StringUtils.isNotBlank(orderField)) {
            if (Constant.DESC.equalsIgnoreCase(order)) {
                queryWrapper.orderByDesc(orderField);
            } else {
                queryWrapper.orderByAsc(orderField);
            }
        } else {
            // 默认按创建时间倒序排序
            queryWrapper.orderByDesc("create_time");
        }
        
        // 执行分页查询
        IPage<BankEntity> pageResult = baseDao.selectPage(pageParam, queryWrapper);
        
        // 转换为DTO
        List<BankDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new BankPageData<>(dtoList, (int) pageResult.getTotal());
    }

    /**
     * 转换为DTO
     */
    public List<BankDTO> convertToDto(List<BankEntity> entityList) {
        return ConvertUtils.sourceToTarget(entityList, BankDTO.class);
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<BankEntity> buildQueryWrapper(Map<String, Object> params) {
        String state = (String) params.get("state");

        QueryWrapper<BankEntity> queryWrapper = new QueryWrapper<BankEntity>();
        
        // 状态筛选
        if (StringUtils.isNotBlank(state)) {
            queryWrapper.eq("state", state);
        }
        return queryWrapper;
    }
}