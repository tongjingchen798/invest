

package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.PayInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户支付信息
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@Mapper
public interface PayInfoDao extends BaseMapper<PayInfoEntity> {

    @Select("select * from tb_pay_info where pay_no=#{payNo}")
    PayInfoEntity selectPayNameByCardNo(@Param("payNo") String payNo);
}
