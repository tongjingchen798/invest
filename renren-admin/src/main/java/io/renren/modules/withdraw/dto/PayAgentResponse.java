package io.renren.modules.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一代付响应结果
 * 
 * @author renren
 * @date 2024-01-01
 */
@Data
@ApiModel(value = "统一代付响应结果")
public class PayAgentResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "响应码")
    private String code;

    @ApiModelProperty(value = "响应描述")
    private String message;

    @ApiModelProperty(value = "第三方订单号")
    private String thirdOrderNo;

    @ApiModelProperty(value = "代付渠道")
    private String channel;

    @ApiModelProperty(value = "原始响应数据")
    private String rawResponse;

    @ApiModelProperty(value = "错误详情")
    private String errorDetail;

    /**
     * 创建成功响应
     */
    public static PayAgentResponse success(String code, String message, String thirdOrderNo, String channel, String rawResponse) {
        PayAgentResponse response = new PayAgentResponse();
        response.setSuccess(true);
        response.setCode(code);
        response.setMessage(message);
        response.setThirdOrderNo(thirdOrderNo);
        response.setChannel(channel);
        response.setRawResponse(rawResponse);
        return response;
    }

    /**
     * 创建失败响应
     */
    public static PayAgentResponse failure(String code, String message, String channel, String rawResponse, String errorDetail) {
        PayAgentResponse response = new PayAgentResponse();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        response.setChannel(channel);
        response.setRawResponse(rawResponse);
        response.setErrorDetail(errorDetail);
        return response;
    }

    /**
     * 创建异常响应
     */
    public static PayAgentResponse error(String message, String channel, String errorDetail) {
        PayAgentResponse response = new PayAgentResponse();
        response.setSuccess(false);
        response.setCode("ERROR");
        response.setMessage(message);
        response.setChannel(channel);
        response.setErrorDetail(errorDetail);
        return response;
    }
}
