package io.renren.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.page.PageData;
import io.renren.common.utils.ConvertUtils;
import io.renren.common.utils.Result;
import io.renren.modules.member.dao.BlacklistDao;
import io.renren.modules.member.dto.BlacklistDTO;
import io.renren.modules.member.entity.BlacklistEntity;
import io.renren.modules.member.service.BlacklistService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 黑白名单服务实现类
 *
 * @author renren
 * @since 1.0.0
 */
@Slf4j
@Service
public class BlacklistServiceImpl extends ServiceImpl<BlacklistDao, BlacklistEntity> implements BlacklistService {

    @Override
    public PageData<BlacklistDTO> getBlacklistPage(Integer page, Integer limit, String mobile, String type) {
        try {
            log.info("开始查询黑白名单，页码: {}, 每页记录数: {}, 手机号: {}, 类型: {}", page, limit, mobile, type);
            
            // 创建MyBatis-Plus分页对象
            Page<BlacklistEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件
            QueryWrapper<BlacklistEntity> queryWrapper = new QueryWrapper<>();
            
            // 手机号筛选
            if (StringUtils.isNotBlank(mobile)) {
                queryWrapper.like("mobile", mobile);
            }
            
            // 类型筛选（黑白名单）
            if (StringUtils.isNotBlank(type)) {
                if ("1".equals(type)) {
                    // 白名单
                    queryWrapper.eq("type", 1);
                } else if ("2".equals(type)) {
                    // 黑名单
                    queryWrapper.eq("type", 2);
                }
            }
            
            // 执行分页查询
            IPage<BlacklistEntity> pageResult = this.page(pageParam, queryWrapper);
            
            // 转换为DTO
            List<BlacklistDTO> dtoList = pageResult.getRecords().stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
            
            // 创建分页数据对象
            PageData<BlacklistDTO> pageData = new PageData<>(dtoList, pageResult.getTotal());
            
            log.info("黑白名单查询成功，共 {} 条记录", pageResult.getTotal());
            return pageData;
            
        } catch (Exception e) {
            log.error("查询黑白名单失败", e);
            throw new RuntimeException("查询黑白名单失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addToBlacklist(Long userId, String mobile, Integer type) {
        try {
            log.info("开始添加用户到黑白名单，用户ID: {}, 手机号: {}, 类型: {}", userId, mobile, type);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (StringUtils.isBlank(mobile)) {
                return new Result().error("会员账号不能为空");
            }
            if (type == null || (type != 1 && type != 2)) {
                return new Result().error("类型必须为1(白名单)或2(黑名单)");
            }
            
            // 检查用户是否已经在黑白名单中
            QueryWrapper<BlacklistEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            BlacklistEntity existingRecord = this.getOne(queryWrapper);
            
            if (existingRecord != null) {
                return new Result().error("用户已在黑白名单中，请先移除后再添加");
            }
            
            // 创建黑白名单记录
            BlacklistEntity blacklistEntity = new BlacklistEntity();
            blacklistEntity.setUserId(userId);
            blacklistEntity.setMobile(mobile.trim());
            blacklistEntity.setType(type);
            
            // 保存记录
            this.save(blacklistEntity);
            
            String typeName = type == 1 ? "白名单" : "黑名单";
            log.info("用户添加到{}成功，用户ID: {}, 记录ID: {}", typeName, userId, blacklistEntity.getId());
            return new Result().ok("用户添加到" + typeName + "成功");
            
        } catch (Exception e) {
            log.error("添加用户到黑白名单失败，用户ID: {}, 类型: {}", userId, type, e);
            throw new RuntimeException("添加用户到黑白名单失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result removeFromBlacklist(Long id) {
        try {
            log.info("开始从黑白名单中移除用户，记录ID: {}", id);
            
            // 参数验证
            if (id == null || id <= 0) {
                return new Result().error("记录ID不能为空");
            }
            
            // 查询记录
            BlacklistEntity blacklistEntity = this.getById(id);
            if (blacklistEntity == null) {
                return new Result().error("黑白名单记录不存在");
            }
            
            // 删除记录
            this.removeById(id);
            
            String typeName = blacklistEntity.getType() == 1 ? "白名单" : "黑名单";
            log.info("用户从{}中移除成功，用户ID: {}, 记录ID: {}", typeName, blacklistEntity.getUserId(), id);
            return new Result().ok("用户从" + typeName + "中移除成功");
            
        } catch (Exception e) {
            log.error("从黑白名单中移除用户失败，记录ID: {}", id, e);
            throw new RuntimeException("从黑白名单中移除用户失败: " + e.getMessage());
        }
    }

    @Override
    public BlacklistEntity checkUserInBlacklist(Long userId) {
        try {
            QueryWrapper<BlacklistEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("type", 2); // 黑名单
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            log.error("检查用户是否在黑名单中失败，用户ID: {}", userId, e);
            return null;
        }
    }

    @Override
    public BlacklistEntity checkUserInWhitelist(Long userId) {
        try {
            QueryWrapper<BlacklistEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                       .eq("type", 1); // 白名单
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            log.error("检查用户是否在白名单中失败，用户ID: {}", userId, e);
            return null;
        }
    }
    
    /**
     * 将BlacklistEntity转换为BlacklistDTO
     */
    private BlacklistDTO convertToDTO(BlacklistEntity entity) {
        BlacklistDTO dto = ConvertUtils.sourceToTarget(entity, BlacklistDTO.class);
        
        // 设置类型名称
        if (entity.getType() != null) {
            dto.setTypeName(entity.getType() == 1 ? "白名单" : "黑名单");
        }
        
        return dto;
    }
}
