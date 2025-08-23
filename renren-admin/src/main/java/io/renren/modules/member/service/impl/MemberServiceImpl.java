package io.renren.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.member.dao.MemberDao;
import io.renren.modules.member.dto.MemberInfoDTO;
import io.renren.modules.member.entity.MemberEntity;
import io.renren.modules.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import io.renren.modules.member.dto.SettlementReportDTO;
import io.renren.modules.finance.entity.UserBalanceDetailEntity;
import io.renren.modules.finance.service.UserBalanceDetailService;
import io.renren.common.utils.Result;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员查询服务实现类
 *
 * @author renren
 * @since 1.0.0
 */
@Slf4j
@Service
public class MemberServiceImpl extends BaseServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private UserBalanceDetailService userBalanceDetailService;

    @Override
    public PageData<MemberInfoDTO> getMemberPage(Integer page, Integer limit, Long agent, Integer balanceFlag,
                                                String biaoqian, Integer biaoqianFlag, String channel, Integer chargeFlag,
                                                Long endTime, Integer liebian, String mobile, String order, String orderField,
                                                Long salesmanid, Long startTime, Integer tzFlag, String username,
                                                Integer vip, Integer viplr, Integer withdrawFlag) {
        
        // 创建MyBatis-Plus分页对象
        Page<MemberEntity> pageParam = new Page<>(page, limit);
        
        // 构建查询条件
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        
        // 代理筛选
        if (agent != null) {
            queryWrapper.eq("agent", agent);
        }
        
        // 余额筛选
        if (balanceFlag != null) {
            if (balanceFlag == 1) {
                queryWrapper.gt("balance", 0); // 有余额
            } else if (balanceFlag == 0) {
                queryWrapper.eq("balance", 0); // 无余额
            }
        }
        
        // 标签筛选
        if (StringUtils.isNotBlank(biaoqian)) {
            queryWrapper.eq("biaoqian", biaoqian);
        }
        
        // 标签存在性筛选
        if (biaoqianFlag != null) {
            if (biaoqianFlag == 1) {
                queryWrapper.isNotNull("biaoqian").ne("biaoqian", ""); // 有标签
            } else if (biaoqianFlag == 0) {
                queryWrapper.and(wrapper -> wrapper.isNull("biaoqian").or().eq("biaoqian", "")); // 无标签
            }
        }
        
        // 渠道筛选
        if (StringUtils.isNotBlank(channel)) {
            queryWrapper.eq("channel", channel);
        }
        
        // 充值筛选
        if (chargeFlag != null) {
            if (chargeFlag == 1) {
                queryWrapper.gt("charge_sum", 0); // 有充值
            } else if (chargeFlag == 0) {
                queryWrapper.eq("charge_sum", 0); // 无充值
            }
        }
        
        // 时间范围筛选
        if (startTime != null) {
            Date startDate = new Date(startTime);
            queryWrapper.ge("create_date", startDate);
        }
        if (endTime != null) {
            Date endDate = new Date(endTime);
            queryWrapper.le("create_date", endDate);
        }
        
        // 裂变筛选
        if (liebian != null) {
            if (liebian == 1) {
                queryWrapper.gt("tgrs", 0); // 是裂变
            } else if (liebian == 0) {
                queryWrapper.eq("tgrs", 0); // 否裂变
            }
        }
        
        // 用户账号筛选
        if (StringUtils.isNotBlank(mobile)) {
            queryWrapper.like("mobile", mobile);
        }
        
        // 业务员筛选
        if (salesmanid != null) {
            queryWrapper.eq("salesmanid", salesmanid);
        }
        
        // 投资筛选
        if (tzFlag != null) {
            if (tzFlag == 1) {
                queryWrapper.gt("history_investment", 0); // 有投资
            } else if (tzFlag == 0) {
                queryWrapper.eq("history_investment", 0); // 无投资
            }
        }
        
        // 用户姓名筛选
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        
        // VIP等级筛选
        if (vip != null) {
            queryWrapper.eq("vip", vip);
        }
        
        // VIP等级范围筛选
        if (viplr != null) {
            if (viplr == 5) {
                queryWrapper.eq("vip", 5); // VIP5
            } else if (viplr == 6) {
                queryWrapper.eq("vip", 6); // VIP6
            } else if (viplr >= 0 && viplr <= 4) {
                queryWrapper.between("vip", 0, 4); // VIP0-4
            }
        }
        
        // 提现筛选
        if (withdrawFlag != null) {
            if (withdrawFlag == 1) {
                queryWrapper.gt("withdraw_sum", 0); // 有提现
            } else if (withdrawFlag == 0) {
                queryWrapper.eq("withdraw_sum", 0); // 无提现
            }
        }
        
        // 排序处理
        if (StringUtils.isNotBlank(orderField)) {
            if (Constant.DESC.equalsIgnoreCase(order)) {
                queryWrapper.orderByDesc(orderField);
            } else {
                queryWrapper.orderByAsc(orderField);
            }
        } else {
            // 默认按创建时间倒序排序
            queryWrapper.orderByDesc("create_date");
        }
        
        // 执行分页查询
        IPage<MemberEntity> pageResult = baseDao.selectPage(pageParam, queryWrapper);
        
        // 转换为DTO
        List<MemberInfoDTO> dtoList = ConvertUtils.sourceToTarget(pageResult.getRecords(), MemberInfoDTO.class);
        
        return new PageData<>(dtoList, pageResult.getTotal());
    }

    @Override
    public PageData<SettlementReportDTO> getSettlementReport(Integer page, Integer limit, Long agent, 
        Long endTime, String order, String orderField, Long salesmanid, Long startTime) {
        
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("agent", agent);
            params.put("endTime", endTime);
            params.put("order", order);
            params.put("orderField", orderField);
            params.put("salesmanid", salesmanid);
            params.put("startTime", startTime);

            // 创建MyBatis-Plus分页对象
            Page<SettlementReportDTO> pageParam = new Page<>(page, limit);
            
            // 调用DAO查询结算报表数据（MyBatis-Plus自动处理分页）
            Page<SettlementReportDTO> result = baseDao.getSettlementReport(pageParam, params);
            
            // 转换为PageData格式
            PageData<SettlementReportDTO> pageData = new PageData<>(result.getRecords(), result.getTotal());
            
            return pageData;
            
        } catch (Exception e) {
            log.error("查询结算报表失败", e);
            throw new RuntimeException("查询结算报表失败: " + e.getMessage());
        }
    }

    @Override
    public java.util.List<String> getBiaoQianList() {
        try {
            // 查询所有非空且非空的标签
            QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("DISTINCT biaoqian")
                       .isNotNull("biaoqian")
                       .ne("biaoqian", "")
                       .orderByAsc("biaoqian");
            
            List<MemberEntity> memberList = baseDao.selectList(queryWrapper);
            
            // 提取标签值并去重
            java.util.List<String> tagList = memberList.stream()
                .map(MemberEntity::getBiaoqian)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toList());
            
            log.info("成功获取标签列表，共 {} 个标签", tagList.size());
            return tagList;
            
        } catch (Exception e) {
            log.error("获取标签列表失败", e);
            throw new RuntimeException("获取标签列表失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result paySalary(Long userId, Long amount) {
        try {
            log.info("开始发放工资，用户ID: {}, 金额: {} 分", userId, amount);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (amount == null || amount <= 0) {
                return new Result().error("工资金额必须大于0");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 更新用户余额
            Long currentBalance = member.getAssets() != null ? member.getAssets() : 0L;
            Long newBalance = currentBalance + amount;
            
            member.setAssets(newBalance);
            this.updateById(member);
            
            // 记录余额明细
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(userId);
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(member.getAgent() != null ? Long.valueOf(member.getAgent()) : null);
            balanceDetail.setAgentName(member.getAgentName());
            balanceDetail.setBusinessType(12); // 12表示工资
            balanceDetail.setChannel("后台发放");
            balanceDetail.setOriginalAmount(amount);
            balanceDetail.setRemarks("工资发放");
            balanceDetail.setSalesmanName(member.getSalesmanName());
            balanceDetail.setSalesmanId(member.getSalesmanid() != null ? Long.valueOf(member.getSalesmanid()) : null);
            balanceDetail.setStatus(1); // 1表示正常
            
            userBalanceDetailService.insert(balanceDetail);
            
            log.info("工资发放成功，用户ID: {}, 金额: {} 分, 新余额: {} 分", userId, amount, newBalance);
            return new Result().ok("工资发放成功");
            
        } catch (Exception e) {
            log.error("工资发放失败，用户ID: {}, 金额: {} 分", userId, amount, e);
            throw new RuntimeException("工资发放失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateUserBiaoqian(Long userId, Integer biaoqian, Integer type) {
        try {
            log.info("开始修改用户标签，用户ID: {}, 标签: {}, 操作类型: {}", userId, biaoqian, type);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (biaoqian == null) {
                return new Result().error("标签值不能为空");
            }
            if (type == null || (type != 1 && type != 2)) {
                return new Result().error("操作类型必须为1(设置标签)或2(清除标签)");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 根据操作类型处理标签
            if (type == 1) {
                // 设置标签
                member.setBiaoqian(String.valueOf(biaoqian));
                log.info("设置用户标签，用户ID: {}, 标签: {}", userId, biaoqian);
            } else if (type == 2) {
                // 清除标签
                member.setBiaoqian(null);
                log.info("清除用户标签，用户ID: {}", userId);
            }
            
            // 更新用户信息
            this.updateById(member);
            
            log.info("用户标签修改成功，用户ID: {}, 标签: {}, 操作类型: {}", userId, biaoqian, type);
            return new Result().ok("标签修改成功");
            
        } catch (Exception e) {
            log.error("用户标签修改失败，用户ID: {}, 标签: {}, 操作类型: {}", userId, biaoqian, type, e);
            throw new RuntimeException("用户标签修改失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateUserToAgent(Long userId, Long salesmanid) {
        try {
            log.info("开始修改用户业务员，用户ID: {}, 业务员ID: {}", userId, salesmanid);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (salesmanid == null || salesmanid <= 0) {
                return new Result().error("业务员ID不能为空");
            }

            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 更新用户业务员信息
            member.setSalesmanid(String.valueOf(salesmanid));
            // 更新用户信息
            this.updateById(member);
            
            log.info("用户业务员修改成功，用户ID: {}, 业务员ID: {}", userId, salesmanid);
            return new Result().ok("业务员修改成功");
            
        } catch (Exception e) {
            log.error("用户业务员修改失败，用户ID: {}, 业务员ID: {}", userId, salesmanid, e);
            throw new RuntimeException("用户业务员修改失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateUp(Long userId, String mobile) {
        try {
            log.info("开始设置用户上级，用户ID: {}, 上级手机号: {}", userId, mobile);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (mobile == null || mobile.trim().isEmpty()) {
                return new Result().error("上级用户手机号不能为空");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 查询上级用户信息
            QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mobile", mobile.trim());
            MemberEntity upMember = this.selectById(queryWrapper);
            if (upMember == null) {
                return new Result().error("上级用户不存在");
            }
            
            // 检查是否设置自己为上级
            if (userId.equals(upMember.getId())) {
                return new Result().error("不能设置自己为上级");
            }
            
            // 更新用户上级信息
            member.setUpinviteCode(upMember.getInviteCode());
            // 更新用户信息
            this.updateById(member);
            
            log.info("用户上级设置成功，用户ID: {}, 上级ID: {}, 上级姓名: {}", userId, upMember.getId(), upMember.getUsername());
            return new Result().ok("上级设置成功");
            
        } catch (Exception e) {
            log.error("设置用户上级失败，用户ID: {}, 上级手机号: {}", userId, mobile, e);
            throw new RuntimeException("设置用户上级失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateUserName(Long userId, String username) {
        try {
            log.info("开始修改用户姓名，用户ID: {}, 新姓名: {}", userId, username);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (username == null || username.trim().isEmpty()) {
                return new Result().error("用户姓名不能为空");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 检查姓名长度限制（假设最大长度为20个字符）
            String trimmedUsername = username.trim();
            if (trimmedUsername.length() > 20) {
                return new Result().error("用户姓名不能超过20个字符");
            }
            
            // 更新用户姓名
            member.setUsername(trimmedUsername);
            
            // 更新用户信息
            this.updateById(member);
            
            log.info("用户姓名修改成功，用户ID: {}, 新姓名: {}", userId, trimmedUsername);
            return new Result().ok("姓名修改成功");
            
        } catch (Exception e) {
            log.error("修改用户姓名失败，用户ID: {}, 姓名: {}", userId, username, e);
            throw new RuntimeException("修改用户姓名失败: " + e.getMessage());
        }
    }
}
