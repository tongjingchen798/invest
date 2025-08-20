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
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会员查询服务实现类
 *
 * @author renren
 * @since 1.0.0
 */
@Slf4j
@Service
public class MemberServiceImpl extends BaseServiceImpl<MemberDao, MemberEntity> implements MemberService {

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
}
