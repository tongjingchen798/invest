package io.renren.modules.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
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
public class MailServiceImpl extends CrudServiceImpl<MailDao, MailEntity, MailDTO> implements MailService {

    /**
     * 重写分页方法，使用最优化的MyBatis-Plus分页实现
     */
    @Override
    public PageData<MailDTO> page(Map<String, Object> params) {
        // 构建查询条件
        QueryWrapper<MailEntity> wrapper = buildQueryWrapper(params);
        
        // 创建分页对象
        Page<MailEntity> page = new Page<>(
            Long.parseLong(params.get(Constant.PAGE).toString()),
            Long.parseLong(params.get(Constant.LIMIT).toString())
        );
        
        // 执行分页查询
        Page<MailEntity> resultPage = baseDao.selectPage(page, wrapper);
        
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

    /**
     * 实现抽象方法getWrapper，调用buildQueryWrapper
     */
    @Override
    public QueryWrapper<MailEntity> getWrapper(Map<String, Object> params) {
        return buildQueryWrapper(params);
    }

}
