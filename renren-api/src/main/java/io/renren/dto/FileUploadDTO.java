 

package io.renren.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传DTO
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "文件上传")
public class FileUploadDTO {
    @ApiModelProperty(value = "文件URL")
    private String url;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "文件类型")
    private String fileType;
}
