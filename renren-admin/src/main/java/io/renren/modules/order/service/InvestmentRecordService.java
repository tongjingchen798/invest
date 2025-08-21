package io.renren.modules.order.service;

import io.renren.common.page.PageData;
import io.renren.modules.order.dto.InvestmentRecordDTO;

import java.util.Map;

/**
 * 购买记录
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
public interface InvestmentRecordService {

    /**
     * 分页查询购买记录
     */
    PageData<InvestmentRecordDTO> selectPage(Map<String, Object> params);

}
