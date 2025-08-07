 

 package io.renren.dto;

 import io.swagger.annotations.ApiModel;
 import io.swagger.annotations.ApiModelProperty;
 import lombok.Data;
 
 import java.io.Serializable;
 import java.util.Date;
 
 /**
  * 用户支付信息
  *
  * @author Mark sunlightcs@gmail.com
  * @since 1.0.0
  */
 @Data
 @ApiModel(value = "用户支付信息")
 public class PayInfoDTO implements Serializable {
     private static final long serialVersionUID = 1L;
 
     @ApiModelProperty(value = "id")
     private Long id;
 
     @ApiModelProperty(value = "用户ID")
     private Long userId;
 
     @ApiModelProperty(value = "银行简称")
     private String blankCode;
 
     @ApiModelProperty(value = "银行名称")
     private String blankName;
 
     @ApiModelProperty(value = "收款人姓名")
     private String payName;
 
     @ApiModelProperty(value = "银行账号")
     private String payNo;
 
     @ApiModelProperty(value = "收款人手机号码")
     private String mobile;
 
     @ApiModelProperty(value = "ifsc")
     private String ifsc;
 
     @ApiModelProperty(value = "状态 0：停用 1：正常")
     private Integer state;
 
     @ApiModelProperty(value = "排序")
     private Integer sortV;
 
     @ApiModelProperty(value = "创建时间")
     private String createTime;
 
     @ApiModelProperty(value = "操作时间")
     private String operTime;
 
     @ApiModelProperty(value = "操作工号")
     private String operCode;
 
     @ApiModelProperty(value = "状态时间")
     private String stateTime;
 
     @ApiModelProperty(value = "渠道")
     private String channel;
 
     @ApiModelProperty(value = "代理")
     private String agent;
 
     @ApiModelProperty(value = "业务员ID")
     private String salesmanid;
 }