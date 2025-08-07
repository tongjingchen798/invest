

package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.PayInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户支付信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Mapper
public interface PayInfoDao extends BaseMapper<PayInfoEntity> {

}
