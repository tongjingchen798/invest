package io.renren.modules.member.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.member.dto.PayInfoDTO;
import io.renren.modules.member.entity.PayInfoEntity;

import java.util.Map;

/**
 * 用户支付信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
public interface PayInfoService extends CrudService<PayInfoEntity, PayInfoDTO> {

    /**
     * 自定义分页查询，支持多表关联查询
     * @param params 查询参数
     * @return 分页数据
     */
    PageData<PayInfoDTO> customPage(Map<String, Object> params);
}