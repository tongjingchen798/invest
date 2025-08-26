package io.renren.modules.balank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.balank.dao.BankDao;
import io.renren.modules.balank.dto.BankDTO;
import io.renren.modules.balank.entity.BankEntity;
import io.renren.modules.balank.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 银行管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Slf4j
@Service
public class BankServiceImpl extends CrudServiceImpl<BankDao, BankEntity, BankDTO> implements BankService {

    @Override
    public QueryWrapper<BankEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        Integer state = null;
        if (params.get("state") != null) {
            state = Integer.parseInt(params.get("state").toString());
        }

        QueryWrapper<BankEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(state != null, "state", state);

        return wrapper;
    }

    @Override
    public PageData<BankDTO> page(Map<String, Object> params) {
        try {
            // 1. 构建分页参数
            Page<BankEntity> page = buildPage(params);

            // 2. 构建查询条件
            QueryWrapper<BankEntity> queryWrapper = buildQueryWrapper(params);

            // 3. 执行分页查询
            IPage<BankEntity> pageResult = baseDao.selectPage(page, queryWrapper);

            // 4. 转换为DTO
            List<BankDTO> dtoList = convertToDTOList(pageResult.getRecords());

            // 5. 构建返回结果
            return new PageData<>(dtoList, pageResult.getTotal());

        } catch (Exception e) {
            log.error("分页查询失败，参数: {}", params, e);
            throw new RuntimeException("分页查询失败: " + e.getMessage());
        }
    }

    /**
     * 构建分页参数
     */
    private Page<BankEntity> buildPage(Map<String, Object> params) {
        Integer pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        Integer pageSize = Integer.valueOf(params.get(Constant.LIMIT).toString());
        return new Page<>(pageNum, pageSize);
    }

    /**
     * 构建基础查询条件
     */
    private QueryWrapper<BankEntity> buildQueryWrapper(Map<String, Object> params) {
        QueryWrapper<BankEntity> queryWrapper = new QueryWrapper<>();
        
        // 状态筛选
        if (params.get("state") != null) {
            queryWrapper.eq("state", params.get("state"));
        }
        
        // 银行名称模糊查询
        if (StringUtils.isNotBlank((String) params.get("bankName"))) {
            queryWrapper.like("bank_name", params.get("bankName"));
        }
        
        // 银行代码精确查询
        if (StringUtils.isNotBlank((String) params.get("bankCode"))) {
            queryWrapper.eq("bank_code", params.get("bankCode"));
        }
        
        // 动态排序
        String orderField = (String) params.get(Constant.ORDER_FIELD);
        String order = (String) params.get(Constant.ORDER);
        if (StringUtils.isNotBlank(orderField)) {
            if (Constant.DESC.equalsIgnoreCase(order)) {
                queryWrapper.orderByDesc(orderField);
            } else {
                queryWrapper.orderByAsc(orderField);
            }
        } else {
            // 默认排序
            queryWrapper.orderByDesc("create_time");
        }
        
        return queryWrapper;
    }

    /**
     * 转换为DTO列表
     */
    private List<BankDTO> convertToDTOList(List<BankEntity> entityList) {
        return entityList.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * 转换为DTO
     */
    private BankDTO convertToDTO(BankEntity entity) {
        BankDTO dto = new BankDTO();
        BeanUtils.copyProperties(entity, dto);
        
//        // 状态转换
//        if (entity.getState() != null) {
//            dto.setStateText(entity.getState() == 1 ? "正常" : "停用");
//        }
//
        return dto;
    }

}