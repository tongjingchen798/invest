package io.renren.modules.usdtrecord.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.page.PageData;
import io.renren.modules.usdtrecord.dto.UsdtRecordDTO;
import io.renren.modules.usdtrecord.entity.UsdtRecordEntity;

import java.util.Map;

/**
 * USDT收款记录服务接口
 *
 * @author renren
 * @since 2024-01-01
 */
public interface UsdtRecordService extends IService<UsdtRecordEntity> {

    /**
     * 分页查询USDT收款记录
     * @param params 查询参数
     * @return 分页数据
     */
    PageData<UsdtRecordDTO> page(Map<String, Object> params);
}
