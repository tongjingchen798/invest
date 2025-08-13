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
            // 获取当前用户信息
            UserEntity currentUser = userDao.selectById(userId);
            if (currentUser == null) {
                throw new RuntimeException("用户不存在");
            }

            MyAgentDTO myAgent = new MyAgentDTO();

            // 设置基本佣金信息
            myAgent.setHirstory_amt(currentUser.getHistoryProfit() != null ? currentUser.getHistoryProfit() : 0L);
            myAgent.setHirstory_gzamt(currentUser.getHistoryProfit() != null ? currentUser.getHistoryProfit() : 0L); // 工资暂时等于佣金
            myAgent.setToday_amt(currentUser.getTodayProfit() != null ? currentUser.getTodayProfit() : 0L);
            myAgent.setToday_gzamt(currentUser.getTodayProfit() != null ? currentUser.getTodayProfit() : 0L); // 今日工资暂时等于今日佣金
            myAgent.setWithdraw_amt(currentUser.getCommissionBalance() != null ? currentUser.getCommissionBalance() : 0L);
            myAgent.setYt_withdraw_amt(currentUser.getWithdrawSum() != null ? currentUser.getWithdrawSum() : 0L);

            // 获取各级会员列表
            myAgent.setList1(getLevel1Members(currentUser.getInviteCode()));
            myAgent.setList2(getLevel2Members(currentUser.getInviteCode()));
            myAgent.setList3(getLevel3Members(currentUser.getInviteCode()));

            // 计算佣金汇总
            calculateCommissionAmounts(myAgent);

            // 计算有效会员数
            calculateMonthlyEffectiveMembers(myAgent, userId);

            return myAgent;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取代理佣金信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取一级会员列表
     */
    private List<AgentMemberDTO> getLevel1Members(String inviteCode) {
        List<AgentMemberDTO> members = new ArrayList<>();
        if (inviteCode == null) {
            return members;
        }

        List<UserEntity> level1Users = userDao.selectBySuperiorId(inviteCode);
        for (UserEntity user : level1Users) {
            AgentMemberDTO member = convertToAgentMember(user, 0.07); // 一级佣金比例7%
            member.setXj(getLevel2Members(user.getInviteCode()));
            members.add(member);
        }
        return members;
    }

    /**
     * 获取二级会员列表
     */
    private List<AgentMemberDTO> getLevel2Members(String inviteCode) {
        List<AgentMemberDTO> members = new ArrayList<>();
        if (inviteCode == null) {
            return members;
        }

        List<UserEntity> level2Users = userDao.selectBySuperiorId(inviteCode);
        for (UserEntity user : level2Users) {
            AgentMemberDTO member = convertToAgentMember(user, 0.03); // 二级佣金比例3%
            member.setXj(getLevel3Members(user.getInviteCode()));
            members.add(member);
        }
        return members;
    }

    /**
     * 获取三级会员列表
     */
    private List<AgentMemberDTO> getLevel3Members(String inviteCode) {
        List<AgentMemberDTO> members = new ArrayList<>();
        if (inviteCode == null) {
            return members;
        }

        List<UserEntity> level3Users = userDao.selectBySuperiorId(inviteCode);
        for (UserEntity user : level3Users) {
            AgentMemberDTO member = convertToAgentMember(user, 0.02); // 三级佣金比例2%
            member.setXj(new ArrayList<>()); // 三级会员没有下级
            members.add(member);
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
     * 计算各级佣金汇总
     */
    private void calculateCommissionAmounts(MyAgentDTO myAgent) {
        long list1Amt = 0;
        long list2Amt = 0;

        // 计算一级佣金
        if (myAgent.getList1() != null) {
            for (AgentMemberDTO member : myAgent.getList1()) {
                if (member.getHirstory_amt() != null) {
                    list1Amt += member.getHirstory_amt() * member.getYj_rate();
                }
            }
        }

        // 计算二级佣金
        if (myAgent.getList2() != null) {
            for (AgentMemberDTO member : myAgent.getList2()) {
                if (member.getHirstory_amt() != null) {
                    list2Amt += member.getHirstory_amt() * member.getYj_rate();
                }
            }
        }

        myAgent.setList1_amt(list1Amt);
        myAgent.setList2_amt(list2Amt);
    }

    /**
     * 计算月度有效会员数
     */
    private void calculateMonthlyEffectiveMembers(MyAgentDTO myAgent, Long userId) {
        try {
            // 获取当前月份
            Calendar cal = Calendar.getInstance();
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            int currentYear = cal.get(Calendar.YEAR);

            // 计算一级有效会员数
            long effectiveList1 = 0;
            long effectiveList1Month = 0;
            if (myAgent.getList1() != null) {
                effectiveList1 = myAgent.getList1().size();
                // 本月有效会员数（这里简化处理，实际应该查询投资记录）
                effectiveList1Month = effectiveList1;
            }

            // 计算二级有效会员数
            long effectiveList2 = 0;
            long effectiveList2Month = 0;
            if (myAgent.getList2() != null) {
                effectiveList2 = myAgent.getList2().size();
                // 本月有效会员数（这里简化处理，实际应该查询投资记录）
                effectiveList2Month = effectiveList2;
            }

            myAgent.setEffectiveList1(effectiveList1);
            myAgent.setEffectiveList1Month(effectiveList1Month);
            myAgent.setEffectiveList2(effectiveList2);
            myAgent.setEffectiveList2Month(effectiveList2Month);

        } catch (Exception e) {
            // 如果计算失败，设置为0
            myAgent.setEffectiveList1(0L);
            myAgent.setEffectiveList1Month(0L);
            myAgent.setEffectiveList2(0L);
            myAgent.setEffectiveList2Month(0L);
        }
    }
}
