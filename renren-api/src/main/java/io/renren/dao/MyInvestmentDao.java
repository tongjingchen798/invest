package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.dto.MyInvestmentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 我的投资DAO接口
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface MyInvestmentDao extends BaseDao<MyInvestmentDTO> {

    /**
     * 查询总数
     * @param params 查询参数
     * @return 总数
     */
    int selectTotal(Map<String, Object> params);

    /**
     * 分页查询列表
     * @param params 查询参数
     * @return 投资列表
     */
    List<MyInvestmentDTO> selectList(Map<String, Object> params);
}
