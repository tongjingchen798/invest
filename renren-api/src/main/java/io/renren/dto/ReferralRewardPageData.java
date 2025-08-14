package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 推荐返利流水分页数据DTO
 *
 * @author renren
 * @email renren@gmail.com
 * @date 2024-01-01 00:00:00
 */
@Data
@ApiModel(value = "推荐返利流水分页数据")
public class ReferralRewardPageData {

    @ApiModelProperty(value = "推荐返利流水列表")
    private List<ReferralRewardDetailDTO> list;

    @ApiModelProperty(value = "汇总信息")
    private Map<String, Object> sum;

    @ApiModelProperty(value = "总记录数")
    private Integer total;

    @ApiModelProperty(value = "当前页码")
    private Integer page;

    @ApiModelProperty(value = "每页大小")
    private Integer limit;
}
