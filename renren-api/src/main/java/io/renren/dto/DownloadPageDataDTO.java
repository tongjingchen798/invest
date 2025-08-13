package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 下载页数据
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "下载页数据")
public class DownloadPageDataDTO {
    
    @ApiModelProperty(value = "访问量")
    private Long fwl;
    
    @ApiModelProperty(value = "下载量")
    private Long xzl;
    
    @ApiModelProperty(value = "今日访问量")
    private Long todayFwl;
    
    @ApiModelProperty(value = "今日下载量")
    private Long todayXzl;
    
    @ApiModelProperty(value = "本月访问量")
    private Long monthFwl;
    
    @ApiModelProperty(value = "本月下载量")
    private Long monthXzl;
    
    @ApiModelProperty(value = "总访问量")
    private Long totalFwl;
    
    @ApiModelProperty(value = "总下载量")
    private Long totalXzl;
}
