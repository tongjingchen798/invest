package io.renren.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.entity.AdvertisementEntity;
import io.renren.dto.AdvertisementDTO;
import io.renren.common.page.PageData;

import java.util.List;
import java.util.Map;

/**
 * 广告素材
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2025-07-10 20:05:01
 */
public interface AdvertisementService extends IService<AdvertisementEntity> {

    /**
     * 根据类型查询启用的广告
     * @param type 广告类型
     * @return 广告DTO列表
     */
    List<AdvertisementDTO> getByType(Integer type);

    /**
     * 查询所有启用的广告
     * @return 广告DTO列表
     */
    List<AdvertisementDTO> getAllEnabled();

    /**
     * 根据生效时间查询广告
     * @return 广告DTO列表
     */
    List<AdvertisementDTO> getByEffectiveTime();

    /**
     * 分页查询广告
     * @param params 查询参数
     * @return 分页数据
     */
    PageData<AdvertisementDTO> getPage(Map<String, Object> params);
}
