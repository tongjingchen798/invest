package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首次提现检查DTO
 *
 * @author renren
 * @date 2024-01-01
 */
@Data
@ApiModel(value = "首次提现检查结果")
public class FirstWithdrawCheckDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 是否首次提现：1-首次提现，0-非首次提现
     */
    @ApiModelProperty(value = "是否首次提现：1-首次提现，0-非首次提现")
    private Integer sc;
    
    public FirstWithdrawCheckDTO() {
    }
    
    public FirstWithdrawCheckDTO(Integer sc) {
        this.sc = sc;
    }
    
    /**
     * 创建首次提现结果
     */
    public static FirstWithdrawCheckDTO firstTime() {
        return new FirstWithdrawCheckDTO(1);
    }
    
    /**
     * 创建非首次提现结果
     */
    public static FirstWithdrawCheckDTO notFirstTime() {
        return new FirstWithdrawCheckDTO(0);
    }
}
