package io.renren.service.impl;

import io.renren.dao.UserDao;
import io.renren.dto.AgentMemberDTO;
import io.renren.dto.MyAgentDTO;
import io.renren.entity.UserEntity;
import io.renren.service.AgentCommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 代理佣金服务实现类
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Service
public class AgentCommissionServiceImpl implements AgentCommissionService {

    @Autowired
    private UserDao userDao;

    @Override
    public MyAgentDTO getMyAgentCommission(Long userId) {
        try {
            MyAgentDTO myAgent = new MyAgentDTO();
            
            // 获取当前用户信息
            UserEntity currentUser = userDao.selectById(userId);
            if (currentUser == null) {
                throw new RuntimeException("用户不存在");
            }
            
            // 设置基础佣金信息（从UserEntity获取）
            myAgent.setHirstory_amt(currentUser.getHistoryProfit() != null ? currentUser.getHistoryProfit() : 0L);
            myAgent.setHirstory_gzamt(0L); // 工资字段在UserEntity中可能没有对应字段
            myAgent.setToday_amt(currentUser.getTodayProfit() != null ? currentUser.getTodayProfit() : 0L);
            myAgent.setToday_gzamt(0L); // 今日工资字段在UserEntity中可能没有对应字段
            myAgent.setWithdraw_amt(currentUser.getBalance() != null ? currentUser.getBalance() : 0L); // 可提现余额
            myAgent.setYt_withdraw_amt(currentUser.getWithdrawSum() != null ? currentUser.getWithdrawSum() : 0L); // 已提现
            myAgent.setList1_amt(0L); // 需要计算一级佣金
            myAgent.setList2_amt(0L); // 需要计算二级佣金
            myAgent.setEffectiveList1(currentUser.getUacnt() != null ? currentUser.getUacnt() : 0L); // 有效一级会员数
            myAgent.setEffectiveList1Month(0L); // 本月有效一级会员数，需要按月统计
            myAgent.setEffectiveList2(currentUser.getUbcnt() != null ? currentUser.getUbcnt() : 0L); // 有效二级会员数
            myAgent.setEffectiveList2Month(0L); // 本月有效二级会员数，需要按月统计
            
            // 设置一级会员列表
            myAgent.setList1(getLevel1Members(currentUser.getInviteCode()));
            
            // 设置二级会员列表
            myAgent.setList2(getLevel2Members(currentUser.getInviteCode()));
            
            // 设置三级会员列表
            myAgent.setList3(getLevel3Members(currentUser.getInviteCode()));
            
            // 计算一级和二级佣金总额
            calculateCommissionAmounts(myAgent);
            
            // 计算本月有效会员数
            calculateMonthlyEffectiveMembers(myAgent, userId);
            
            return myAgent;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取佣金信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取一级会员列表
     */
    private List<AgentMemberDTO> getLevel1Members(String inviteCode) {
        List<AgentMemberDTO> members = new ArrayList<>();
        
        try {
            // 从UserEntity查询一级会员（直接下级）
            List<UserEntity> level1Users = userDao.selectBySuperiorId(inviteCode);
            
            for (UserEntity user : level1Users) {
                AgentMemberDTO member = convertToAgentMember(user, 0.07); // 一级佣金比例7%
                
                // 设置下级会员
                member.setXj(getLevel2Members(user.getInviteCode()));
                
                members.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 如果查询失败，返回空列表
        }
        
        return members;
    }

    /**
     * 获取二级会员列表
     */
    private List<AgentMemberDTO> getLevel2Members(String inviteCode) {
        List<AgentMemberDTO> members = new ArrayList<>();
        
        try {
            // 从UserEntity查询二级会员
            List<UserEntity> level2Users = userDao.selectBySuperiorId(inviteCode);
            
            for (UserEntity user : level2Users) {
                AgentMemberDTO member = convertToAgentMember(user, 0.03); // 二级佣金比例3%
                
                // 设置下级会员
                member.setXj(getLevel3Members(user.getInviteCode()));
                
                members.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 如果查询失败，返回空列表
        }
        
        return members;
    }

    /**
     * 获取三级会员列表
     */
    private List<AgentMemberDTO> getLevel3Members(String inviteCode) {
        List<AgentMemberDTO> members = new ArrayList<>();
        
        try {
            // 从UserEntity查询三级会员
            List<UserEntity> level3Users = userDao.selectBySuperiorId(inviteCode);
            
            for (UserEntity user : level3Users) {
                AgentMemberDTO member = convertToAgentMember(user, 0.02); // 三级佣金比例2%
                members.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 如果查询失败，返回空列表
        }
        
        return members;
    }

    /**
     * 将UserEntity转换为AgentMemberDTO
     */
    private AgentMemberDTO convertToAgentMember(UserEntity user, Double commissionRate) {
        AgentMemberDTO member = new AgentMemberDTO();
        member.setId(user.getId());
        member.setUsername(user.getUsername());
        member.setVip(user.getVip() != null ? user.getVip() : 0);
        member.setHirstory_amt(user.getHistoryProfit() != null ? user.getHistoryProfit() : 0L);
        member.setHirstory_tz(user.getHistoryInvestment() != null ? user.getHistoryInvestment() : 0L);
        member.setToday_amt(user.getTodayProfit() != null ? user.getTodayProfit() : 0L);
        member.setToday_tz(0L); // 今日投资需要从投资记录表查询
        member.setYj_rate(commissionRate);
        member.setXj(new ArrayList<>());
        return member;
    }

    /**
     * 计算一级和二级佣金总额
     */
    private void calculateCommissionAmounts(MyAgentDTO myAgent) {
        long list1Total = 0;
        long list2Total = 0;
        
        // 计算一级佣金总额
        if (myAgent.getList1() != null) {
            for (AgentMemberDTO member : myAgent.getList1()) {
                if (member.getHirstory_amt() != null) {
                    list1Total += member.getHirstory_amt();
                }
            }
        }
        
        // 计算二级佣金总额
        if (myAgent.getList2() != null) {
            for (AgentMemberDTO member : myAgent.getList2()) {
                if (member.getHirstory_amt() != null) {
                    list2Total += member.getHirstory_amt();
                }
            }
        }
        
        myAgent.setList1_amt(list1Total);
        myAgent.setList2_amt(list2Total);
    }

    /**
     * 计算本月有效会员数
     */
    private void calculateMonthlyEffectiveMembers(MyAgentDTO myAgent, Long userId) {
        try {
            // 获取本月开始时间
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date monthStart = cal.getTime();
            
            // 这里可以添加按月统计的逻辑
            // 目前设置为0，实际项目中需要根据业务需求实现
            myAgent.setEffectiveList1Month(0L);
            myAgent.setEffectiveList2Month(0L);
            
        } catch (Exception e) {
            e.printStackTrace();
            // 如果计算失败，设置为0
            myAgent.setEffectiveList1Month(0L);
            myAgent.setEffectiveList2Month(0L);
        }
    }
}
