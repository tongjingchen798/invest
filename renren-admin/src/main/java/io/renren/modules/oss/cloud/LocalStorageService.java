package io.renren.modules.oss.cloud;

import io.renren.common.exception.RenException;
import io.renren.common.utils.DateUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * 本地存储服务
 * 
 * @author lip
 * @date 2024-01-01
 */
@Service
public class LocalStorageService {
    
    @Value("${local.storage.path:/uploads}")
    private String storagePath;
    
    @Value("${local.storage.url:http://localhost:8080/uploads}")
    private String storageUrl;
    
    /**
     * 上传文件到本地存储
     * 
     * @param file 文件
     * @return 文件访问URL
     */
    public String upload(MultipartFile file) {
        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            String fileName = generateFileName(extension);
            
            // 创建目录结构：年/月/日
            String datePath = DateUtils.format(new Date(), "yyyy/MM/dd");
            String fullPath = storagePath + "/" + datePath;
            
            // 确保目录存在
            Path directory = Paths.get(fullPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            
            // 保存文件
            Path filePath = directory.resolve(fileName);
            Files.write(filePath, file.getBytes());
            
            // 返回访问URL
            return storageUrl + "/" + datePath + "/" + fileName;
            
        } catch (IOException e) {
            throw new RenException("文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传文件到本地存储（指定路径）
     * 
     * @param file 文件
     * @param path 指定路径
     * @return 文件访问URL
     */
    public String upload(MultipartFile file, String path) {
        try {
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            String fileName = generateFileName(extension);
            
            // 创建完整路径
            String fullPath = storagePath + "/" + path;
            
            // 确保目录存在
            Path directory = Paths.get(fullPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            
            // 保存文件
            Path filePath = directory.resolve(fileName);
            Files.write(filePath, file.getBytes());
            
            // 返回访问URL
            return storageUrl + "/" + path + "/" + fileName;
            
        } catch (IOException e) {
            throw new RenException("文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除文件
     * 
     * @param url 文件URL
     * @return 是否删除成功
     */
    public boolean delete(String url) {
        try {
            // 从URL中提取文件路径
            String relativePath = url.replace(storageUrl + "/", "");
            String fullPath = storagePath + "/" + relativePath;
            
            Path filePath = Paths.get(fullPath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            }
            return false;
            
        } catch (IOException e) {
            throw new RenException("文件删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成唯一文件名
     * 
     * @param extension 文件扩展名
     * @return 文件名
     */
    private String generateFileName(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + "." + extension;
    }
    
    /**
     * 检查文件类型是否允许
     * 
     * @param fileName 文件名
     * @return 是否允许
     */
    public boolean isAllowedFileType(String fileName) {
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        String[] allowedTypes = {"jpg", "jpeg", "png", "gif", "bmp", "webp", "pdf", "doc", "docx", "xls", "xlsx", "txt"};
        
        for (String type : allowedTypes) {
            if (type.equals(extension)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查文件大小是否允许
     * 
     * @param fileSize 文件大小（字节）
     * @return 是否允许
     */
    public boolean isAllowedFileSize(long fileSize) {
        // 默认最大10MB
        long maxSize = 10 * 1024 * 1024;
        return fileSize <= maxSize;
    }
}
