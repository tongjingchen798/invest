package io.renren.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.entity.UsdtRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * U收款记录表
 *
 * @author renren
 * @since 2024-01-01
 */
@Mapper
public interface UsdtRecordDao extends BaseMapper<UsdtRecordEntity> {

    /**
     * 根据交易Hash检查是否存在
     * @param txHash 交易Hash
     * @return 存在返回true，不存在返回false
     */
    @Select("SELECT COUNT(1) FROM usdtrecord WHERE transaction_id = #{txHash}")
    boolean existsByTxHash(@Param("txHash") String txHash);
    
    /**
     * 根据交易Hash和网络检查是否存在
     * @param txHash 交易Hash
     * @param network 网络类型
     * @return 存在返回true，不存在返回false
     */
    @Select("SELECT COUNT(1) FROM usdtrecord WHERE transaction_id = #{txHash} AND network = #{network}")
    boolean existsByTxHashAndNetwork(@Param("txHash") String txHash, @Param("network") String network);

}
