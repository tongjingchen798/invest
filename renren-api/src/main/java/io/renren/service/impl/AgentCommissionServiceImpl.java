package io.renren.service.impl;

import io.renren.dto.AgentMemberDTO;
import io.renren.dto.MyAgentDTO;
import io.renren.service.AgentCommissionService;
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

    @Override
    public MyAgentDTO getMyAgentCommission(Long userId) {
        try {
            MyAgentDTO myAgent = new MyAgentDTO();
            
            // 设置基础佣金信息（这里使用模拟数据，实际项目中应该从数据库查询）
            myAgent.setHirstory_amt(0L);
            myAgent.setHirstory_gzamt(0L);
            myAgent.setToday_amt(0L);
            myAgent.setToday_gzamt(0L);
            myAgent.setWithdraw_amt(0L);
            myAgent.setYt_withdraw_amt(0L);
            myAgent.setList1_amt(0L);
            myAgent.setList2_amt(0L);
            myAgent.setEffectiveList1(0L);
            myAgent.setEffectiveList1Month(0L);
            myAgent.setEffectiveList2(0L);
            myAgent.setEffectiveList2Month(0L);
            
            // 设置一级会员列表
            myAgent.setList1(getLevel1Members(userId));
            
            // 设置二级会员列表
            myAgent.setList2(getLevel2Members(userId));
            
            // 设置三级会员列表
            myAgent.setList3(getLevel3Members(userId));
            
            return myAgent;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取佣金信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取一级会员列表
     */
    private List<AgentMemberDTO> getLevel1Members(Long userId) {
        List<AgentMemberDTO> members = new ArrayList<>();
        
        // 这里应该从数据库查询一级会员信息
        // 目前使用模拟数据
        AgentMemberDTO member1 = createMockMember(1001L, "user001", 1, 1000L, 50000L, 100L, 5000L, 0.05);
        AgentMemberDTO member2 = createMockMember(1002L, "user002", 2, 2000L, 80000L, 200L, 8000L, 0.05);
        
        // 设置下级会员
        member1.setXj(getLevel2Members(member1.getId()));
        member2.setXj(getLevel2Members(member2.getId()));
        
        members.add(member1);
        members.add(member2);
        
        return members;
    }

    /**
     * 获取二级会员列表
     */
    private List<AgentMemberDTO> getLevel2Members(Long parentId) {
        List<AgentMemberDTO> members = new ArrayList<>();
        
        // 这里应该从数据库查询二级会员信息
        // 目前使用模拟数据
        AgentMemberDTO member1 = createMockMember(2001L, "subuser001", 1, 500L, 20000L, 50L, 2000L, 0.03);
        AgentMemberDTO member2 = createMockMember(2002L, "subuser002", 1, 800L, 30000L, 80L, 3000L, 0.03);
        
        // 设置下级会员
        member1.setXj(getLevel3Members(member1.getId()));
        member2.setXj(getLevel3Members(member2.getId()));
        
        members.add(member1);
        members.add(member2);
        
        return members;
    }

    /**
     * 获取三级会员列表
     */
    private List<AgentMemberDTO> getLevel3Members(Long parentId) {
        List<AgentMemberDTO> members = new ArrayList<>();
        
        // 这里应该从数据库查询三级会员信息
        // 目前使用模拟数据
        AgentMemberDTO member1 = createMockMember(3001L, "subsubuser001", 0, 200L, 10000L, 20L, 1000L, 0.02);
        AgentMemberDTO member2 = createMockMember(3002L, "subsubuser002", 0, 300L, 15000L, 30L, 1500L, 0.02);
        
        members.add(member1);
        members.add(member2);
        
        return members;
    }

    /**
     * 创建模拟会员数据
     */
    private AgentMemberDTO createMockMember(Long id, String username, Integer vip, 
                                          Long historyAmt, Long historyTz, 
                                          Long todayAmt, Long todayTz, Double yjRate) {
        AgentMemberDTO member = new AgentMemberDTO();
        member.setId(id);
        member.setUsername(username);
        member.setVip(vip);
        member.setHirstory_amt(historyAmt);
        member.setHirstory_tz(historyTz);
        member.setToday_amt(todayAmt);
        member.setToday_tz(todayTz);
        member.setYj_rate(yjRate);
        member.setXj(new ArrayList<>());
        return member;
    }
}
