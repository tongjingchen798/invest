package io.renren.dto;

import lombok.Data;

/**
 * 渠道分配结果DTO
 *
 * @author renren
 * @since 2024-01-01
 */
@Data
public class ChannelAllocationResult {

    /**
     * 业务员ID
     */
    private Long salesmanId;

    /**
     * 业务员名称
     */
    private String salesmanName;

    /**
     * 代理ID
     */
    private Long agentId;

    /**
     * 代理名称
     */
    private String agentName;

    /**
     * 渠道ID
     */
    private Long channel;

    /**
     * 是否分配成功
     */
    private boolean success;

    /**
     * 分配失败原因
     */
    private String errorMessage;

    public ChannelAllocationResult() {
    }

    public ChannelAllocationResult(Long salesmanId, String salesmanName, Long agentId, String agentName, Long channel) {
        this.salesmanId = salesmanId;
        this.salesmanName = salesmanName;
        this.agentId = agentId;
        this.agentName = agentName;
        this.channel = channel;
        this.success = true;
    }

    public static ChannelAllocationResult success(Long salesmanId, String salesmanName, Long agentId, String agentName, Long channel) {
        return new ChannelAllocationResult(salesmanId, salesmanName, agentId, agentName, channel);
    }

    public static ChannelAllocationResult failure(String errorMessage) {
        ChannelAllocationResult result = new ChannelAllocationResult();
        result.success = false;
        result.errorMessage = errorMessage;
        return result;
    }
}
