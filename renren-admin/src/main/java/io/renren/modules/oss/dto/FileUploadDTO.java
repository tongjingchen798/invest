package io.renren.modules.oss.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 文件上传DTO
 * 
 * @author lip
 * @date 2024-01-01
 */
@ApiModel(value = "文件上传DTO")
public class FileUploadDTO {
    
    @ApiModelProperty(value = "文件URL")
    private String url;
    
    @ApiModelProperty(value = "文件名")
    private String fileName;
    
    @ApiModelProperty(value = "文件大小（字节）")
    private Long fileSize;
    
    @ApiModelProperty(value = "上传时间")
    private Long uploadTime;
    
    @ApiModelProperty(value = "文件类型")
    private String fileType;
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public Long getUploadTime() {
        return uploadTime;
    }
    
    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
