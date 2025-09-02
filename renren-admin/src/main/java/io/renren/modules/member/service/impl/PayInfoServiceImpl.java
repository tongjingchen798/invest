package io.renren.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.member.dao.PayInfoDao;
import io.renren.modules.member.dto.PayInfoDTO;
import io.renren.modules.member.entity.PayInfoEntity;
import io.renren.modules.member.service.PayInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户支付信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Service
public class PayInfoServiceImpl extends CrudServiceImpl<PayInfoDao, PayInfoEntity, PayInfoDTO> implements PayInfoService {

        @Override
    public QueryWrapper<PayInfoEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<PayInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<PayInfoDTO> customPage(Map<String, Object> params) {
        // 创建MyBatis-Plus分页对象
        long curPage = 1;
        long limit = 10;
        
        if (params.get(Constant.PAGE) != null) {
            curPage = Long.parseLong((String) params.get(Constant.PAGE));
        }
        if (params.get(Constant.LIMIT) != null) {
            limit = Long.parseLong((String) params.get(Constant.LIMIT));
        }
        
        // 创建分页对象，注意这里使用PayInfoDTO作为泛型
        Page<PayInfoDTO> page = new Page<>(curPage, limit);
        
        // 调用自定义的XML查询方法
        IPage<PayInfoDTO> pageResult = baseDao.selectPayInfoPage(page, params);
        
        // 转换为PageData格式
        return new PageData<>(pageResult.getRecords(), pageResult.getTotal());
    }

}