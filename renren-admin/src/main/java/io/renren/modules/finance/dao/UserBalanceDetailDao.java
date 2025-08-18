package io.renren.modules.finance.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.modules.finance.dto.UserBalanceDetailDTO;
import io.renren.modules.finance.entity.UserBalanceDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 用户余额明细DAO
 *
 * @author renren
 * @since 1.0.0
 */
@Mapper
public interface UserBalanceDetailDao extends BaseMapper<UserBalanceDetailEntity> {
    
    /**
     * 分页查询账变明细（关联用户表）
     *
     * @param page 分页参数
     * @param biaoqian 标签
     * @param biaoqianFlag 标签标志
     * @param busiType 业务类型
     * @param endTime 结束时间
     * @param mobile 手机号
     * @param startTime 开始时间
     * @return 分页结果
     */
    IPage<UserBalanceDetailDTO> selectBalanceDetailPage(Page<UserBalanceDetailEntity> page,
                                                        @Param("biaoqian") String biaoqian,
                                                        @Param("biaoqianFlag") Integer biaoqianFlag,
                                                        @Param("busiType") Integer busiType,
                                                        @Param("endTime") Date endTime,
                                                        @Param("mobile") String mobile,
                                                        @Param("startTime") Date startTime);
}
