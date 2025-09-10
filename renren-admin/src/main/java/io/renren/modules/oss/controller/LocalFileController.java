package io.renren.modules.oss.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.exception.ErrorCode;
import io.renren.common.utils.Result;
import io.renren.modules.oss.cloud.LocalStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地文件上传控制器
 * 
 * @author lip
 * @date 2024-01-01
 */
@RestController
@RequestMapping("sys/oss")
@Api(tags = "本地文件上传")
public class LocalFileController {
    
    @Autowired
    private LocalStorageService localStorageService;
    
    @PostMapping("/upload")
    @ApiOperation("上传文件到本地存储")
    @LogOperation("上传文件到本地存储")
    public Result<Map<String, Object>> uploadFile(
            @ApiParam(value = "文件", required = true) 
            @RequestParam("file") MultipartFile file) {
        
        // 检查文件是否为空
        if (file.isEmpty()) {
            return new Result<Map<String, Object>>().error(ErrorCode.UPLOAD_FILE_EMPTY);
        }
        
        // 检查文件类型
        if (!localStorageService.isAllowedFileType(file.getOriginalFilename())) {
            return new Result<Map<String, Object>>().error("不支持的文件类型");
        }
        
        // 检查文件大小
        if (!localStorageService.isAllowedFileSize(file.getSize())) {
            return new Result<Map<String, Object>>().error("文件大小超过限制（最大10MB）");
        }
        
        try {
            // 上传文件
            String url = localStorageService.upload(file);
            
            Map<String, Object> data = new HashMap<>();
            data.put("src", url);
            return new Result<Map<String, Object>>().ok(data);
            
        } catch (Exception e) {
            return new Result<Map<String, Object>>().error("文件上传失败: " + e.getMessage());
        }
    }

    
    @DeleteMapping("/delete")
    @ApiOperation("删除文件")
    @LogOperation("删除文件")
    public Result<Object> deleteFile(
            @ApiParam(value = "文件URL", required = true) 
            @RequestParam String url) {
        
        try {
            boolean success = localStorageService.delete(url);
            if (success) {
                return new Result<Object>().ok("文件删除成功");
            } else {
                return new Result<Object>().error("文件不存在或删除失败");
            }
        } catch (Exception e) {
            return new Result<Object>().error("文件删除失败: " + e.getMessage());
        }
    }
}
