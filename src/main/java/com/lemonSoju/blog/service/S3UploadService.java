//package com.lemonSoju.blog.service;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class S3UploadService {
//
//    private final AmazonS3 amazonS3;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    public String saveFile(MultipartFile multipartFile) throws IOException {
//        String originalFilename = generateUniqueFileName(multipartFile.getOriginalFilename());
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(multipartFile.getSize());
//        metadata.setContentType(multipartFile.getContentType());
//        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
//        return amazonS3.getUrl(bucket, originalFilename).toString();
//    }
//
//    public String generateUniqueFileName(String originalFilename) {
//        String extension = getFileExtension(originalFilename);
//        String uniqueFileName = UUID.randomUUID().toString() + extension;
//        return uniqueFileName;
//    }
//
//    public String getFileExtension(String fileName) {
//        int dotIndex = fileName.lastIndexOf('.');
//        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
//            return fileName.substring(dotIndex);
//        }
//        return "";
//    }
//}