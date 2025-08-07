
package io.renren.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.dao.PayInfoDao;
import io.renren.dto.PayInfoDTO;
import io.renren.dto.PayInfoPageData;
import io.renren.dto.ProjectDTO;
import io.renren.entity.PayInfoEntity;
import io.renren.service.PayInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户支付信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Service
public class PayInfoServiceImpl extends BaseServiceImpl<PayInfoDao, PayInfoEntity> implements PayInfoService {

    @Override
    public PayInfoPageData<PayInfoDTO> queryPageData(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<PayInfoEntity> queryWrapper = buildQueryWrapper(params);
        
        // 使用 BaseServiceImpl 的标准分页处理
        IPage<PayInfoEntity> pageResult = baseDao.selectPage(
            getPage(params, Constant.CREATE_DATE, false),
            queryWrapper
        );
        
        // 转换为DTO
        List<PayInfoDTO> dtoList = convertToDto(pageResult.getRecords());
        
        return new PayInfoPageData<>(dtoList, (int) pageResult.getTotal());
    }

    /**
     * 转换为DTO
     */
    public List<PayInfoDTO> convertToDto(List<PayInfoEntity> entityList) {
        return ConvertUtils.sourceToTarget(entityList, PayInfoDTO.class);
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<PayInfoEntity> buildQueryWrapper(Map<String, Object> params) {
        String userId = (String) params.get("userId");

        QueryWrapper<PayInfoEntity> queryWrapper = new QueryWrapper<PayInfoEntity>();
        
        // 用户ID筛选
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        
        // 只查询正常状态的数据
        queryWrapper.eq("state", 1);
        
        // 默认按创建时间倒序排序
        queryWrapper.orderByDesc(Constant.CREATE_DATE);

        return queryWrapper;
    }

    @Override
    public void delete(List<Long> ids) {
        // 批量删除支付信息
        baseDao.deleteBatchIds(ids);
    }
}
