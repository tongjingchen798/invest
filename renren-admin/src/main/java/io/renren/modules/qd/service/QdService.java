package io.renren.modules.qd.service;

import io.renren.common.page.PageData;
import io.renren.modules.qd.dto.QdRecordDTO;

import java.util.Map;

/**
 * 签到记录服务接口
 *
 * @author renren
 * @since 2024-01-01
 */
public interface QdService {

    /**
     * 获取签到记录列表
     * @param params 查询参数
     * @return 分页数据
     */
    PageData<QdRecordDTO> getQdRecordList(Map<String, Object> params);
}
