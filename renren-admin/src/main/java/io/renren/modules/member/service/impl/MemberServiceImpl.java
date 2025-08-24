package io.renren.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.member.dao.MemberDao;
import io.renren.modules.member.dto.AmountToBeCashedDTO;
import io.renren.modules.member.dto.FissionRewardDTO;
import io.renren.modules.member.dto.MemberInfoDTO;
import io.renren.modules.member.entity.MemberEntity;
import io.renren.modules.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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
            balanceDetail.setAgentId(member.getAgent());
            balanceDetail.setAgentName(member.getAgentName());
            balanceDetail.setBusiType(12); // 12表示工资
            balanceDetail.setChannel("1");
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
    public Result updateUserBiaoqian(Long userId, String biaoqian, Integer type) {
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
                member.setBiaoqian(biaoqian);
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
            member.setSalesmanid(salesmanid);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addBalance(Long userId, Long amount, Integer balanceType, String remark) {
        try {
            log.info("开始手工调整余额，用户ID: {}, 金额: {} 分, 操作类型: {}, 备注: {}", userId, amount, balanceType, remark);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (amount == null || amount <= 0) {
                return new Result().error("金额必须大于0");
            }
            if (balanceType == null || (balanceType != 1 && balanceType != 2)) {
                return new Result().error("操作类型必须为1(增加余额)或2(减少余额)");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 获取当前余额
            Long currentBalance = member.getAssets() != null ? member.getAssets() : 0L;
            Long newBalance;
            String operationType;
            Integer businessType = 7; // 7表示手工充值，8表示手工扣款
            
            // 根据操作类型调整余额
            if (balanceType == 1) {
                // 增加余额
                newBalance = currentBalance + amount;
                operationType = "增加余额";
                businessType = 7; // 手工充值
            } else {
                // 减少余额
                if (currentBalance < amount) {
                    return new Result().error("用户余额不足，当前余额: " + currentBalance + " 分，需要扣除: " + amount + " 分");
                }
                newBalance = currentBalance - amount;
                operationType = "减少余额";
                businessType = 8; // 手工扣款
            }
            
            // 更新用户余额
            member.setAssets(newBalance);
            this.updateById(member);
            
            // 记录余额明细
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(userId);
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(member.getAgent());
            balanceDetail.setAgentName(member.getAgentName());
            balanceDetail.setBusiType(businessType); // 7表示手工充值，8表示手工扣款
            balanceDetail.setChannel("后台手工调整");
            balanceDetail.setOriginalAmount(amount);
            balanceDetail.setRemarks(remark != null ? remark : operationType);
            balanceDetail.setSalesmanName(member.getSalesmanName());
            balanceDetail.setSalesmanId(member.getSalesmanid() != null ? Long.valueOf(member.getSalesmanid()) : null);
            balanceDetail.setStatus(1); // 1表示正常
            
            userBalanceDetailService.insert(balanceDetail);
            
            log.info("余额调整成功，用户ID: {}, 原余额: {} 分, 调整金额: {} 分, 新余额: {} 分, 操作类型: {}", 
                    userId, currentBalance, amount, newBalance, operationType);
            return new Result().ok("余额调整成功");
            
        } catch (Exception e) {
            log.error("手工调整余额失败，用户ID: {}, 金额: {}, 操作类型: {}", userId, amount, balanceType, e);
            throw new RuntimeException("手工调整余额失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result freeBalance(Long userId, Long amount, Integer balanceType, String remark) {
        try {
            log.info("开始操作冻结金额，用户ID: {}, 金额: {} 分, 操作类型: {}, 备注: {}", userId, amount, balanceType, remark);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (amount == null || amount <= 0) {
                return new Result().error("金额必须大于0");
            }
            if (balanceType == null || (balanceType != 1 && balanceType != 2)) {
                return new Result().error("操作类型必须为1(冻结金额)或2(解冻金额)");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 获取当前余额和冻结金额
            Long currentBalance = member.getAssets() != null ? member.getAssets() : 0L;
            Long currentFrozenBalance = member.getFreezeBalance() != null ? member.getFreezeBalance() : 0L;
            Long newBalance, newFrozenBalance;
            String operationType;
            Integer businessType = 5; // 5表示冻结金额，6表示解冻金额
            
            // 根据操作类型处理冻结金额
            if (balanceType == 1) {
                // 冻结金额
                if (currentBalance < amount) {
                    return new Result().error("用户可用余额不足，当前余额: " + currentBalance + " 分，需要冻结: " + amount + " 分");
                }
                newBalance = currentBalance - amount;
                newFrozenBalance = currentFrozenBalance + amount;
                operationType = "冻结金额";
                businessType = 5; // 冻结金额
            } else {
                // 解冻金额
                if (currentFrozenBalance < amount) {
                    return new Result().error("用户冻结余额不足，当前冻结余额: " + currentFrozenBalance + " 分，需要解冻: " + amount + " 分");
                }
                newBalance = currentBalance + amount;
                newFrozenBalance = currentFrozenBalance - amount;
                operationType = "解冻金额";
                businessType = 6; // 解冻金额
            }
            
            // 更新用户余额和冻结余额
            member.setAssets(newBalance);
            member.setFreezeBalance(newFrozenBalance);
            this.updateById(member);
            
            // 记录余额明细
            UserBalanceDetailEntity balanceDetail = new UserBalanceDetailEntity();
            balanceDetail.setUserId(userId);
            balanceDetail.setTransactionDate(new Date());
            balanceDetail.setAgentId(member.getAgent());
            balanceDetail.setAgentName(member.getAgentName());
            balanceDetail.setBusiType(businessType); // 5表示冻结金额，6表示解冻金额
            balanceDetail.setChannel("后台冻结操作");
            balanceDetail.setOriginalAmount(amount);
            balanceDetail.setRemarks(remark != null ? remark : operationType);
            balanceDetail.setSalesmanName(member.getSalesmanName());
            balanceDetail.setSalesmanId(member.getSalesmanid() != null ? Long.valueOf(member.getSalesmanid()) : null);
            balanceDetail.setStatus(1); // 1表示正常
            
            userBalanceDetailService.insert(balanceDetail);
            
            log.info("冻结金额操作成功，用户ID: {}, 原余额: {} 分, 原冻结余额: {} 分, 操作金额: {} 分, 新余额: {} 分, 新冻结余额: {} 分, 操作类型: {}", 
                    userId, currentBalance, currentFrozenBalance, amount, newBalance, newFrozenBalance, operationType);
            return new Result().ok("冻结金额操作成功");
            
        } catch (Exception e) {
            log.error("操作冻结金额失败，用户ID: {}, 金额: {}, 操作类型: {}", userId, amount, balanceType, e);
            throw new RuntimeException("操作冻结金额失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatePws(Long userId, String password) {
        try {
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return new Result().error("新密码不能为空");
            }
            
            // 检查密码长度限制（假设最小长度为6个字符，最大长度为20个字符）
            String trimmedPassword = password.trim();
            if (trimmedPassword.length() < 6) {
                return new Result().error("密码长度不能少于6个字符");
            }
            if (trimmedPassword.length() > 20) {
                return new Result().error("密码长度不能超过20个字符");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            member.setPassword(DigestUtils.sha256Hex(trimmedPassword));
            // 更新用户信息
            this.updateById(member);
            
            log.info("用户密码修改成功，用户ID: {}", userId);
            return new Result().ok("密码修改成功");
            
        } catch (Exception e) {
            log.error("修改用户密码失败，用户ID: {}", userId, e);
            throw new RuntimeException("修改用户密码失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatestatus(Long userId, Integer status, String biaoqian) {
        try {
            log.info("开始更新用户状态，用户ID: {}, 状态: {}, 标签: {}", userId, status, biaoqian);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (status == null || (status != 0 && status != 1)) {
                return new Result().error("状态值必须为0(禁用)或1(启用)");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 更新用户状态
            member.setStatus(status);
            
            // 如果提供了标签，则同时更新标签
            if (biaoqian != null) {
                member.setBiaoqian(biaoqian.trim());
                log.info("同时更新用户标签，用户ID: {}, 标签: {}", userId, biaoqian);
            }
            
            // 更新用户信息
            this.updateById(member);
            
            String statusText = status == 1 ? "启用" : "禁用";
            log.info("用户状态更新成功，用户ID: {}, 状态: {}", userId, statusText);
            return new Result().ok("用户" + statusText + "成功");
            
        } catch (Exception e) {
            log.error("更新用户状态失败，用户ID: {}, 状态: {}", userId, status, e);
            throw new RuntimeException("更新用户状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatetzrewardWithdrawStatus(Long userId, Integer status) {
        try {
            log.info("开始更新佣金账户提现状态，用户ID: {}, 状态: {}", userId, status);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (status == null || (status != 0 && status != 1)) {
                return new Result().error("状态值必须为0(禁用提现)或1(启用提现)");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 更新佣金账户提现状态
            member.setRewardWithdrawStatus(status);
            // 更新用户信息
            this.updateById(member);
            
            String statusText = status == 1 ? "启用提现" : "禁用提现";
            log.info("佣金账户提现状态更新成功，用户ID: {}, 状态: {}", userId, statusText);
            return new Result().ok("佣金账户提现" + statusText + "成功");
            
        } catch (Exception e) {
            log.error("更新佣金账户提现状态失败，用户ID: {}, 状态: {}", userId, status, e);
            throw new RuntimeException("更新佣金账户提现状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatetzWithdrawStatus(Long userId, Integer status) {
        try {
            log.info("开始更新投资账户提现状态，用户ID: {}, 状态: {}", userId, status);
            
            // 参数验证
            if (userId == null || userId <= 0) {
                return new Result().error("用户ID不能为空");
            }
            if (status == null || (status != 0 && status != 1)) {
                return new Result().error("状态值必须为0(禁用提现)或1(启用提现)");
            }
            
            // 查询用户信息
            MemberEntity member = this.selectById(userId);
            if (member == null) {
                return new Result().error("用户不存在");
            }
            
            // 更新投资账户提现状态
            member.setTzWithdrawStatus(status);
            
            // 更新用户信息
            this.updateById(member);
            
            String statusText = status == 1 ? "启用提现" : "禁用提现";
            log.info("投资账户提现状态更新成功，用户ID: {}, 状态: {}", userId, statusText);
            return new Result().ok("投资账户提现" + statusText + "成功");
            
        } catch (Exception e) {
            log.error("更新投资账户提现状态失败，用户ID: {}, 状态: {}", userId, status, e);
            throw new RuntimeException("更新投资账户提现状态失败: " + e.getMessage());
        }
    }

    @Override
    public PageData<FissionRewardDTO> getFissionRewardPage(Integer page, Integer limit, Long agent, String biaoqian,
                                                           Integer biaoqianFlag, Integer cce3Flag, Long endTime,
                                                           Integer gzFlag, Integer llFlag, String mobile, String order,
                                                           Integer rewardFlag, Long salesmanid, Long startTime,
                                                           Integer xjFlag, Integer ytrewardFlag, Integer zcFlag) {
        try {
            log.info("开始查询裂变佣金，页码: {}, 每页记录数: {}, 代理: {}, 手机号: {}, 排序: {}", 
                    page, limit, agent, mobile, order);
            
            // 创建MyBatis-Plus分页对象
            Page<MemberEntity> pageParam = new Page<>(page, limit);
            
            // 构建查询条件
            QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
            
            // 代理筛选
            if (agent != null) {
                queryWrapper.eq("agent", agent);
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
            
            // CCE3返佣筛选 - 字段不存在，暂时跳过
            // if (cce3Flag != null) {
            //     if (cce3Flag == 1) {
            //         queryWrapper.gt("cce3fl", 0); // 有CCE3返佣
            //     } else if (cce3Flag == 0) {
            //         queryWrapper.eq("cce3fl", 0); // 无CCE3返佣
            //     }
            // }
            
            // 工资筛选 - 字段不存在，暂时跳过
            // if (gzFlag != null) {
            //     if (gzFlag == 1) {
            //         queryWrapper.gt("gz", 0); // 有工资
            //     } else if (gzFlag == 0) {
            //         queryWrapper.eq("gz", 0); // 无工资
            //     }
            // }
            
            // 浏览筛选 - 字段不存在，暂时跳过
            // if (llFlag != null) {
            //     if (llFlag == 1) {
            //         queryWrapper.gt("fxfl", 0); // 有访问奖励
            //     } else if (llFlag == 0) {
            //         queryWrapper.eq("fxfl", 0); // 无访问奖励
            //     }
            // }
            
            // 手机号筛选
            if (StringUtils.isNotBlank(mobile)) {
                queryWrapper.like("mobile", mobile);
            }
            
            // 佣金余额筛选
            if (rewardFlag != null) {
                if (rewardFlag == 1) {
                    queryWrapper.gt("commission_balance", 0); // 有佣金余额
                } else if (rewardFlag == 0) {
                    queryWrapper.eq("commission_balance", 0); // 无佣金余额
                }
            }
            
            // 业务员筛选
            if (salesmanid != null) {
                queryWrapper.eq("salesmanid", salesmanid);
            }
            
            // 下级筛选
            if (xjFlag != null) {
                if (xjFlag == 1) {
                    queryWrapper.gt("tgrs", 0); // 有下级
                } else if (xjFlag == 0) {
                    queryWrapper.eq("tgrs", 0); // 无下级
                }
            }
            
            // 已提佣金筛选 - 字段不存在，暂时跳过
            // if (ytrewardFlag != null) {
            //     if (ytrewardFlag == 1) {
            //         queryWrapper.gt("yt_reward", 0); // 有已提佣金
            //     } else if (ytrewardFlag == 0) {
            //         queryWrapper.eq("yt_reward", 0); // 无已提佣金
            //     }
            // }
            
            // 注册筛选 - 字段不存在，暂时跳过
            // if (zcFlag != null) {
            //     if (zcFlag == 1) {
            //         queryWrapper.gt("zcfl", 0); // 有注册奖励
            //     } else if (zcFlag == 0) {
            //         queryWrapper.eq("zcfl", 0); // 无注册奖励
            //     }
            // }
            
            // 时间范围筛选
            if (startTime != null) {
                queryWrapper.ge("create_time", new Date(startTime));
            }
            if (endTime != null) {
                queryWrapper.le("create_time", new Date(endTime));
            }
            
            // 排序处理
            if (StringUtils.isNotBlank(order)) {
                if (Constant.DESC.equalsIgnoreCase(order)) {
                    queryWrapper.orderByDesc("create_time");
                } else {
                    queryWrapper.orderByAsc("create_time");
                }
            } else {
                // 默认按创建时间倒序排序
                queryWrapper.orderByDesc("create_date");
            }
            
            // 执行分页查询
            IPage<MemberEntity> pageResult = baseDao.selectPage(pageParam, queryWrapper);
            
            // 转换为DTO
            List<FissionRewardDTO> dtoList = pageResult.getRecords().stream()
                .map(this::convertToFissionRewardDTO)
                .collect(java.util.stream.Collectors.toList());
            
            // 创建分页数据对象
            PageData<FissionRewardDTO> pageData = new PageData<>(dtoList, pageResult.getTotal());
            
            log.info("裂变佣金查询成功，共 {} 条记录", pageResult.getTotal());
            return pageData;
            
        } catch (Exception e) {
            log.error("查询裂变佣金失败", e);
            throw new RuntimeException("查询裂变佣金失败: " + e.getMessage());
        }
    }
    
    /**
     * 将MemberEntity转换为FissionRewardDTO
     */
    private FissionRewardDTO convertToFissionRewardDTO(MemberEntity entity) {
        FissionRewardDTO dto = new FissionRewardDTO();
        
        // 基本信息
        dto.setMobile(entity.getMobile());
        dto.setAgent(entity.getAgent() != null ? String.valueOf(entity.getAgent()) : "");
        dto.setAgentName(entity.getAgentName());
        dto.setBiaoqian(entity.getBiaoqian());
        dto.setSalesmanid(entity.getSalesmanid());
        dto.setSalesmanName(entity.getSalesmanName());
        
        // 佣金相关 - 使用实际存在的字段或设置默认值
        dto.setOneReward(0); // 默认值，实际字段不存在
        dto.setTwoReward(0); // 默认值，实际字段不存在
        dto.setThreeReward(0); // 默认值，实际字段不存在
        dto.setTotalReward(0); // 默认值，实际字段不存在
        dto.setRewardBalance(entity.getCommissionBalance() != null ? entity.getCommissionBalance().intValue() : 0);
        dto.setYtReward(0); // 默认值，实际字段不存在
        dto.setZztReward(0); // 默认值，实际字段不存在
        
        // 奖励相关 - 使用实际存在的字段或设置默认值
        dto.setCce3fl(0); // 默认值，实际字段不存在
        dto.setFxfl(0); // 默认值，实际字段不存在
        dto.setGz(0); // 默认值，实际字段不存在
        dto.setVipjl(entity.getVip() != null ? entity.getVip() : 0);
        dto.setZcfl(0); // 默认值，实际字段不存在
        
        // 其他信息 - 使用实际存在的字段或设置默认值
        dto.setInvestmentAmount(entity.getHistoryInvestment() != null ? entity.getHistoryInvestment().intValue() : 0);
        dto.setInviteCodeStatus(entity.getInviteCodeStatus());
        dto.setXjzhCnt(entity.getTgrs() != null ? entity.getTgrs().intValue() : 0);
        
        return dto;
    }

    @Override
    public PageData<AmountToBeCashedDTO> getAmountToBeCashedPage(Integer page, Integer limit, Long agent, Long salesmanid,
                                                                 Long startTime, Long endTime, String order, String orderField) {
        try {
            log.info("开始查询即将兑付金额，页码: {}, 每页记录数: {}, 代理: {}, 业务员: {}, 开始时间: {}, 结束时间: {}, 排序: {}, 排序字段: {}", 
                    page, limit, agent, salesmanid, startTime, endTime, order, orderField);

            // 创建分页参数
            Page<MemberEntity> pageParam = new Page<>(page, limit);
            QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();

            // 代理筛选
            if (agent != null) {
                queryWrapper.eq("agent", agent);
            }

            // 业务员筛选
            if (salesmanid != null) {
                queryWrapper.eq("salesmanid", salesmanid);
            }

            // 时间范围筛选
            if (startTime != null) {
                queryWrapper.ge("create_date", new Date(startTime));
            }
            if (endTime != null) {
                queryWrapper.le("create_date", new Date(endTime));
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
            List<AmountToBeCashedDTO> dtoList = pageResult.getRecords().stream()
                .map(this::convertToAmountToBeCashedDTO)
                .collect(java.util.stream.Collectors.toList());

            // 创建分页数据对象
            PageData<AmountToBeCashedDTO> pageData = new PageData<>(dtoList, pageResult.getTotal());
            
            log.info("即将兑付金额查询成功，共 {} 条记录", pageResult.getTotal());
            return pageData;
            
        } catch (Exception e) {
            log.error("查询即将兑付金额失败", e);
            throw new RuntimeException("查询即将兑付金额失败: " + e.getMessage());
        }
    }

    /**
     * 将MemberEntity转换为AmountToBeCashedDTO
     */
    private AmountToBeCashedDTO convertToAmountToBeCashedDTO(MemberEntity entity) {
        AmountToBeCashedDTO dto = new AmountToBeCashedDTO();
        
        // 设置兑付金额（这里使用历史收益作为示例，实际业务逻辑可能需要调整）
        dto.setProfitAmount(entity.getHistoryProfit() != null ? entity.getHistoryProfit().intValue() : 0);
        
        // 设置兑付日期（这里使用创建日期作为示例，实际业务逻辑可能需要调整）
        dto.setProfitDate(entity.getCreateDate() != null ? entity.getCreateDate().toString() : "");
        
        return dto;
    }
}
