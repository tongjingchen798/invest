package io.renren.service.impl;

import io.renren.dao.UserDao;
import io.renren.dto.AgentMemberDTO;
import io.renren.dto.MyAgentDTO;
import io.renren.entity.UserEntity;
import io.renren.service.AgentCommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

            // 获取一级会员列表（包含下级）
            myAgent.setList1(getLevel1Members(currentUser.getInviteCode()));

            // 计算佣金汇总
            calculateCommissionAmounts(myAgent);

            // 计算有效会员数
            calculateEffectiveMembers(myAgent);

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
     * 获取二级会员列表（作为一级会员的下级）
     */
    private List<AgentMemberDTO> getLevel2Members(String inviteCode) {
        List<AgentMemberDTO> members = new ArrayList<>();
        if (inviteCode == null) {
            return members;
        }

        List<UserEntity> level2Users = userDao.selectBySuperiorId(inviteCode);
        for (UserEntity user : level2Users) {
            AgentMemberDTO member = convertToAgentMember(user, 0.03); // 二级佣金比例3%
            members.add(member);
        }
        return members;
    }

    /**
     * 将UserEntity转换为AgentMemberDTO
     */
    private AgentMemberDTO convertToAgentMember(UserEntity user, Double commissionRate) {
        AgentMemberDTO member = new AgentMemberDTO();
        member.setId(String.valueOf(user.getId())); // 转换为String类型
        member.setUsername(user.getUsername());
        member.setVip(user.getVip() != null ? user.getVip() : 0);
        member.setHirstory_tz(String.valueOf(user.getHistoryInvestment() != null ? user.getHistoryInvestment() : 0L)); // 转换为String类型
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
                // 从投资金额计算佣金（这里简化处理，实际应该根据佣金比例计算）
                if (member.getHirstory_tz() != null) {
                    try {
                        long investment = Long.parseLong(member.getHirstory_tz());
                        list1Amt += investment * 0.07; // 一级佣金比例7%
                    } catch (NumberFormatException e) {
                        // 如果转换失败，跳过
                    }
                }
            }
        }

        // 计算二级佣金（从一级会员的下级计算）
        if (myAgent.getList1() != null) {
            for (AgentMemberDTO level1Member : myAgent.getList1()) {
                if (level1Member.getXj() != null) {
                    for (AgentMemberDTO level2Member : level1Member.getXj()) {
                        if (level2Member.getHirstory_tz() != null) {
                            try {
                                long investment = Long.parseLong(level2Member.getHirstory_tz());
                                list2Amt += investment * 0.03; // 二级佣金比例3%
                            } catch (NumberFormatException e) {
                                // 如果转换失败，跳过
                            }
                        }
                    }
                }
            }
        }

        myAgent.setList1_amt(list1Amt);
        myAgent.setList2_amt(list2Amt);
    }

    /**
     * 计算有效会员数
     */
    private void calculateEffectiveMembers(MyAgentDTO myAgent) {
        try {
            // 计算一级有效会员数
            long effectiveList1 = 0;
            if (myAgent.getList1() != null) {
                effectiveList1 = myAgent.getList1().size();
            }

            // 计算二级有效会员数
            long effectiveList2 = 0;
            if (myAgent.getList1() != null) {
                for (AgentMemberDTO level1Member : myAgent.getList1()) {
                    if (level1Member.getXj() != null) {
                        effectiveList2 += level1Member.getXj().size();
                    }
                }
            }

            myAgent.setEffectiveList1(effectiveList1);
            myAgent.setEffectiveList2(effectiveList2);

        } catch (Exception e) {
            // 如果计算失败，设置为0
            myAgent.setEffectiveList1(0L);
            myAgent.setEffectiveList2(0L);
        }
    }
}
