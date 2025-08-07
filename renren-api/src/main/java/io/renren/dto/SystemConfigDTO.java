

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统配置DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "系统配置")
public class SystemConfigDTO {
    @ApiModelProperty(value = "系统版本")
    private String version;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "是否维护中")
    private Boolean maintenance;

    @ApiModelProperty(value = "维护信息")
    private String maintenanceInfo;

    @ApiModelProperty(value = "客服联系方式")
    private String customerService;

    @ApiModelProperty(value = "关于我们")
    private String aboutUs;

    @ApiModelProperty(value = "用户协议")
    private String userAgreement;

    @ApiModelProperty(value = "隐私政策")
    private String privacyPolicy;
}
