package io.renren.modules.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.modules.member.dto.BlacklistDTO;
import io.renren.modules.member.entity.BlacklistEntity;

/**
 * 黑白名单服务接口
 *
 * @author renren
 * @since 1.0.0
 */
public interface BlacklistService extends IService<BlacklistEntity> {

    /**
     * 分页查询黑白名单
     *
     * @param page 当前页码
     * @param limit 每页记录数
     * @param mobile 会员账号
     * @param type 类型：1-白名单，2-黑名单，不传查所有
     * @return 黑白名单分页数据
     */
    PageData<BlacklistDTO> getBlacklistPage(Integer page, Integer limit, String mobile, String type);

    /**
     * 添加用户到黑白名单
     *
     * @param mobile 会员账号
     * @param type 类型：1-白名单，2-黑名单
     * @return 操作结果
     */
    Result addToBlacklist( String mobile, Integer type);

    /**
     * 从黑白名单中移除用户
     *
     * @param id 黑白名单记录ID
     * @return 操作结果
     */
    Result removeFromBlacklist(Long id);

//    /**
//     * 检查用户是否在黑名单中
//     *
//     * @param userId 用户ID
//     * @return 黑名单记录，如果不在黑名单中返回null
//     */
//    BlacklistEntity checkUserInBlacklist(Long userId);
//
//    /**
//     * 检查用户是否在白名单中
//     *
//     * @param userId 用户ID
//     * @return 白名单记录，如果不在白名单中返回null
//     */
//    BlacklistEntity checkUserInWhitelist(Long userId);
}
