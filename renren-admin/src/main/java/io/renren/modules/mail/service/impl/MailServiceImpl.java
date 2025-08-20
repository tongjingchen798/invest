package io.renren.modules.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.page.PageData;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.mail.dao.MailDao;
import io.renren.modules.mail.dto.MailDTO;
import io.renren.modules.mail.entity.MailEntity;
import io.renren.modules.mail.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 站内信
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
@Service
public class MailServiceImpl extends ServiceImpl<MailDao, MailEntity> implements MailService {

    /**
     * 分页查询
     */
    @Override
    public PageData<MailDTO> page(Map<String, Object> params) {
        // 获取分页参数，提供默认值
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
        QueryWrapper<MailEntity> wrapper = buildQueryWrapper(params);
        
        // 创建分页对象
        Page<MailEntity> page = new Page<>(current, size);
        
        // 执行分页查询
        Page<MailEntity> resultPage = baseMapper.selectPage(page, wrapper);
        
        // 转换为PageData
        return new PageData<>(
            ConvertUtils.sourceToTarget(resultPage.getRecords(), MailDTO.class),
            resultPage.getTotal()
        );
    }

    /**
     * 构建查询条件
     */
    private QueryWrapper<MailEntity> buildQueryWrapper(Map<String, Object> params) {
        QueryWrapper<MailEntity> wrapper = new QueryWrapper<>();
        
        // 标题查询：当theme不为空时进行模糊查询
        String theme = getStringValue(params, "theme");
        if (StringUtils.isNotBlank(theme)) {
            wrapper.like("theme", theme);
        }
        
        // 发件人查询：当fromUser不为空时进行模糊查询
        String fromUser = getStringValue(params, "fromUser");
        if (StringUtils.isNotBlank(fromUser)) {
            wrapper.like("from_user", fromUser);
        }
        
        // 时间范围查询：当startTime不为空时进行大于等于查询
        Long startTime = getLongValue(params, "startTime");
        if (startTime != null) {
            wrapper.ge("send_time", new Date(startTime));
        }
        
        // 时间范围查询：当endTime不为空时进行小于等于查询
        Long endTime = getLongValue(params, "endTime");
        if (endTime != null) {
            wrapper.le("send_time", new Date(endTime));
        }
        
        // 默认按发送时间倒序
        wrapper.orderByDesc("send_time");

        return wrapper;
    }

    /**
     * 安全获取字符串参数值
     */
    private String getStringValue(Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value == null) {
            return null;
        }
        return value.toString();
    }
    
    /**
     * 安全获取Long类型参数值
     */
    private Long getLongValue(Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
