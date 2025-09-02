package io.renren.modules.member.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.dao.BaseDao;
import io.renren.modules.member.dto.PayInfoDTO;
import io.renren.modules.member.entity.PayInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 用户支付信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-20
 */
@Mapper
public interface PayInfoDao extends BaseDao<PayInfoEntity> {
	
	/**
	 * 自定义分页查询，支持多表关联查询
	 * @param page 分页参数
	 * @param params 查询参数
	 * @return 分页结果
	 */
	IPage<PayInfoDTO> selectPayInfoPage(Page<PayInfoDTO> page, @Param("params") Map<String, Object> params);
}