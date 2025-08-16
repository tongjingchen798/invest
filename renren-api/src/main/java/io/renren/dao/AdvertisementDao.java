package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.AdvertisementEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 广告素材
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2025-07-10 20:05:01
 */
@Mapper
public interface AdvertisementDao extends BaseMapper<AdvertisementEntity> {

    /**
     * 根据类型查询启用的广告
     * @param type 广告类型
     * @return 广告列表
     */
    List<AdvertisementEntity> selectByType(@Param("type") Integer type);

    /**
     * 查询所有启用的广告
     * @return 广告列表
     */
    List<AdvertisementEntity> selectEnabled();

    /**
     * 根据生效时间查询广告
     * @param currentTime 当前时间
     * @return 广告列表
     */
    List<AdvertisementEntity> selectByEffectiveTime(@Param("currentTime") String currentTime);
}
