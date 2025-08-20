package io.renren.modules.paychannel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.paychannel.dao.PayChannelDao;
import io.renren.modules.paychannel.dto.PayChannelDTO;
import io.renren.modules.paychannel.entity.PayChannelEntity;
import io.renren.modules.paychannel.service.PayChannelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付渠道表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class PayChannelServiceImpl extends CrudServiceImpl<PayChannelDao, PayChannelEntity, PayChannelDTO> implements PayChannelService {

    /**
     * 使用selectPage实现分页查询
     */
    @Override
    public PageData<PayChannelDTO> selectPage(Map<String, Object> params) {
        // 获取分页参数
        long current = 1;
        long size = 10;
        
        if (params.get("page") != null) {
            try {
                current = Long.parseLong(params.get("page").toString());
            } catch (NumberFormatException e) {
                current = 1;
            }
        }
        
        if (params.get("limit") != null) {
            try {
                size = Long.parseLong(params.get("limit").toString());
            } catch (NumberFormatException e) {
                size = 10;
            }
        }
        
        // 构建查询条件
        QueryWrapper<PayChannelEntity> wrapper = buildQueryWrapper(params);
        
        // 创建分页对象
        Page<PayChannelEntity> page = new Page<>(current, size);
        
        // 执行分页查询
        Page<PayChannelEntity> resultPage = baseDao.selectPage(page, wrapper);
        
        // 转换为PageData
        return new PageData<>(
            ConvertUtils.sourceToTarget(resultPage.getRecords(), PayChannelDTO.class),
            resultPage.getTotal()
        );
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<PayChannelEntity> buildQueryWrapper(Map<String, Object> params) {
        QueryWrapper<PayChannelEntity> wrapper = new QueryWrapper<>();
        
//        // 渠道名称查询
//        String channelName = (String) params.get("channelName");
//        if (StringUtils.isNotBlank(channelName)) {
//            wrapper.like("channel_name", channelName);
//        }
//
//        // 渠道类型查询
//        String channelType = (String) params.get("channelType");
//        if (StringUtils.isNotBlank(channelType)) {
//            wrapper.eq("channel_type", channelType);
//        }
//
//        // 商户名称查询
//        String merchantname = (String) params.get("merchantname");
//        if (StringUtils.isNotBlank(merchantname)) {
//            wrapper.like("merchantname", merchantname);
//        }
//
//        // 状态查询
//        Object statusObj = params.get("status");
//        if (statusObj != null) {
//            try {
//                Integer status = Integer.valueOf(statusObj.toString());
//                wrapper.eq("status", status);
//            } catch (NumberFormatException e) {
//                // 忽略格式错误的参数
//            }
//        }
//
        // 充提类型查询
        Object chargeorwithdrawObj = params.get("chargeorwithdraw");
        if (chargeorwithdrawObj != null) {
            try {
                Integer chargeorwithdraw = Integer.valueOf(chargeorwithdrawObj.toString());
                wrapper.eq("chargeorwithdraw", chargeorwithdraw);
            } catch (NumberFormatException e) {
                // 忽略格式错误的参数
            }
        }
        
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_date");

        return wrapper;
    }

    @Override
    public QueryWrapper<PayChannelEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<PayChannelEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

}