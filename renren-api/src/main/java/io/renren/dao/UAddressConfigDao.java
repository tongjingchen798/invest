package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.UAddressConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * U地址配置表
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface UAddressConfigDao extends BaseMapper<UAddressConfigEntity> {

    /**
     * 获取可用的tr20充值地址
     * @return
     */
    @Select("select * from u_address_config where state=1 limit 1")
    UAddressConfigEntity selectAddrLimit();
}
