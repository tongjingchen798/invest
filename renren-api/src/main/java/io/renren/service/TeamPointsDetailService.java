package io.renren.service;

import io.renren.dto.TeamPointsDetailPageData;

/**
 * 积分明细服务接口
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
public interface TeamPointsDetailService {

    /**
     * 获取积分明细分页数据
     *
     * @param userId 用户ID
     * @param page   当前页码
     * @param limit  每页记录数
     * @return 积分明细分页数据
     */
    TeamPointsDetailPageData getTeamPointsDetailPageData(Long userId, Integer page, Integer limit);
}
