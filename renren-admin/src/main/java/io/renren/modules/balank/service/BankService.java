package io.renren.modules.balank.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.balank.dto.BankDTO;
import io.renren.modules.balank.entity.BankEntity;

import java.util.Map;

/**
 * 银行管理表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
public interface BankService extends CrudService<BankEntity, BankDTO> {

    /**
     * 联表分页查询
     * @param params 查询参数
     * @return 分页数据
     */
    PageData<BankDTO> page(Map<String, Object> params);

}