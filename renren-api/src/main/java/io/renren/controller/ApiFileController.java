///**
// * Copyright (c) 2018 人人开源 All rights reserved.
// *
// * https://www.renren.io
// *
// * 版权所有，侵权必究！
// */
//
//package io.renren.controller;
//
//import io.renren.annotation.LoginUser;
//import io.renren.common.utils.Result;
//import io.renren.dto.FileUploadDTO;
//import io.renren.entity.UserEntity;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.UUID;
//
///**
// * 文件上传接口
// *
// * @author Mark sunlightcs@gmail.com
// */
//@RestController
//@RequestMapping("/api/file")
//@Api(tags = "文件上传接口")
//public class ApiFileController {
//
//    @PostMapping("upload")
//    @ApiOperation("文件上传")
//    public Result<FileUploadDTO> uploadFile(@LoginUser UserEntity user,
//                                           @RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return new Result<FileUploadDTO>().error("文件不能为空");
//        }
//
//        // 检查文件大小（限制为10MB）
//        if (file.getSize() > 10 * 1024 * 1024) {
//            return new Result<FileUploadDTO>().error("文件大小不能超过10MB");
//        }
//
//        // 检查文件类型
//        String originalFilename = file.getOriginalFilename();
//        String fileExtension = "";
//        if (originalFilename != null && originalFilename.contains(".")) {
//            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        }
//
//        // 允许的文件类型
//        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".pdf", ".doc", ".docx"};
//        boolean isAllowed = false;
//        for (String ext : allowedExtensions) {
//            if (ext.equalsIgnoreCase(fileExtension)) {
//                isAllowed = true;
//                break;
//            }
//        }
//
//        if (!isAllowed) {
//            return new Result<FileUploadDTO>().error("不支持的文件类型");
//        }
//
//        try {
//            // 生成唯一文件名
//            String fileName = UUID.randomUUID().toString() + fileExtension;
//
//            // 创建上传目录
//            String uploadDir = "uploads/";
//            File dir = new File(uploadDir);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // 保存文件
//            File dest = new File(uploadDir + fileName);
//            file.transferTo(dest);
//
//            // 返回文件信息
//            FileUploadDTO fileInfo = new FileUploadDTO();
//            fileInfo.setUrl("/uploads/" + fileName);
//            fileInfo.setFileName(originalFilename);
//            fileInfo.setFileSize(file.getSize());
//            fileInfo.setFileType(file.getContentType());
//
//            return new Result<FileUploadDTO>().ok(fileInfo);
//
//        } catch (IOException e) {
//            return new Result<FileUploadDTO>().error("文件上传失败");
//        }
//    }
//
//    @PostMapping("uploadImage")
//    @ApiOperation("图片上传")
//    public Result<FileUploadDTO> uploadImage(@LoginUser UserEntity user,
//                                            @RequestParam("image") MultipartFile image) {
//        if (image.isEmpty()) {
//            return new Result<FileUploadDTO>().error("图片不能为空");
//        }
//
//        // 检查文件大小（限制为5MB）
//        if (image.getSize() > 5 * 1024 * 1024) {
//            return new Result<FileUploadDTO>().error("图片大小不能超过5MB");
//        }
//
//        // 检查文件类型
//        String originalFilename = image.getOriginalFilename();
//        String fileExtension = "";
//        if (originalFilename != null && originalFilename.contains(".")) {
//            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        }
//
//        // 只允许图片类型
//        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif"};
//        boolean isAllowed = false;
//        for (String ext : allowedExtensions) {
//            if (ext.equalsIgnoreCase(fileExtension)) {
//                isAllowed = true;
//                break;
//            }
//        }
//
//        if (!isAllowed) {
//            return new Result<FileUploadDTO>().error("只支持jpg、jpeg、png、gif格式的图片");
//        }
//
//        try {
//            // 生成唯一文件名
//            String fileName = "img_" + UUID.randomUUID().toString() + fileExtension;
//
//            // 创建上传目录
//            String uploadDir = "uploads/images/";
//            File dir = new File(uploadDir);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // 保存文件
//            File dest = new File(uploadDir + fileName);
//            image.transferTo(dest);
//
//            // 返回文件信息
//            FileUploadDTO fileInfo = new FileUploadDTO();
//            fileInfo.setUrl("/uploads/images/" + fileName);
//            fileInfo.setFileName(originalFilename);
//            fileInfo.setFileSize(image.getSize());
//            fileInfo.setFileType(image.getContentType());
//
//            return new Result<FileUploadDTO>().ok(fileInfo);
//
//        } catch (IOException e) {
//            return new Result<FileUploadDTO>().error("图片上传失败");
//        }
//    }
//}
