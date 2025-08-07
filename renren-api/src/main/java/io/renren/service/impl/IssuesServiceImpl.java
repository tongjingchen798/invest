

package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.renren.common.constant.Constant;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.dao.IssuesDao;
import io.renren.dto.IssuesDTO;
import io.renren.dto.IssuesPageData;
import io.renren.entity.IssuesEntity;
import io.renren.service.IssuesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 广告/图片管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("issuesService")
public class IssuesServiceImpl extends BaseServiceImpl<IssuesDao, IssuesEntity> implements IssuesService {


    @Override
    public IssuesPageData<IssuesDTO> queryPageData(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<IssuesEntity> queryWrapper = buildQueryWrapper(params);
        
        // 使用 BaseServiceImpl 的标准分页处理
        IPage<IssuesEntity> pageResult = baseDao.selectPage(
            getPage(params, Constant.CREATE_DATE, false),
            queryWrapper
        );
        
        // 转换为DTO
        List<IssuesDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new IssuesPageData<>(dtoList, (int) pageResult.getTotal());
    }

    public List<IssuesDTO> convertToDto(List<IssuesEntity> entityList) {
        return ConvertUtils.sourceToTarget(entityList, IssuesDTO.class);
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<IssuesEntity> buildQueryWrapper(Map<String, Object> params) {
        String type = (String) params.get("type");

        QueryWrapper<IssuesEntity> queryWrapper = new QueryWrapper<IssuesEntity>();
        
        // 类型筛选
        if (StringUtils.isNotBlank(type)) {
            queryWrapper.eq("type", type);
        }
        
        // 只查询启用的数据
        queryWrapper.eq("status", 1);
        
        // 默认按排序字段和创建时间排序
        queryWrapper.orderByAsc("sort").orderByDesc(Constant.CREATE_DATE);

        return queryWrapper;
    }
}