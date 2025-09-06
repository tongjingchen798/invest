package io.renren.modules.withdraw.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * WePay代付响应DTO
 * 
 * @author renren
 * @date 2024-01-01
 */
@ApiModel("WePay代付响应参数")
public class PayoutResponseDTO {
    
    @ApiModelProperty(value = "状态码")
    private Integer code;
    
    @ApiModelProperty(value = "状态描述")
    private String desc;
    
    @ApiModelProperty(value = "状态信息")
    private String msg;
    
    @ApiModelProperty(value = "请求状态")
    private Boolean success;
    
    @ApiModelProperty(value = "返回数据")
    private Data data;
    
    /**
     * 返回数据内部类
     */
    @ApiModel("代付返回数据")
    public static class Data {
        
        @ApiModelProperty(value = "系统订单号")
        private String id;
        
        @ApiModelProperty(value = "商户订单号")
        private String orderNo;
        
        // Getters and Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getOrderNo() {
            return orderNo;
        }
        
        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
    }
    
    // Getters and Setters
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public Boolean getSuccess() {
        return success;
    }
    
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    public Data getData() {
        return data;
    }
    
    public void setData(Data data) {
        this.data = data;
    }
}
