package com.smartNotes.util;



import com.smartNotes.exception.BaseException;
import com.smartNotes.exception.file.FileException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioUtil {

    private final MinioClient minioClient;

    public String upload(MultipartFile file, String fileName, String bucket) {
        // 1. 前置校验
        if (file == null || file.isEmpty()) {
            throw new FileException("上传文件不能为空");
        }
        if (fileName == null ) {
            throw new FileException("文件名称不能为空");
        }
        if (bucket == null ) {
            throw new FileException("存储桶名称不能为空");
        }

        // 2. 安全生成唯一文件名
        int lastDotIndex = fileName.lastIndexOf(".");
        String objectName;
        if (lastDotIndex == -1) {
            // 无后缀文件
            objectName = UUID.randomUUID().toString();
        } else {
            objectName = UUID.randomUUID() + fileName.substring(lastDotIndex);
        }

        // 3. 执行上传并处理异常
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("文件上传成功: bucket={}, objectName={}", bucket, objectName);
            return objectName;
        } catch (Exception e) {
            log.error("文件上传失败: bucket={}, fileName={}, 原因: {}", bucket, fileName, e.getMessage(), e);
            // 关键：将原始异常作为 cause 传递，方便全局异常处理器打印堆栈
            throw new BaseException("上传文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件流
     * @param objectName 文件对象名称
     * @param bucketName 存储桶名称
     * @return 文件流
     */
    public Resource getResource(@NotBlank String objectName,@NotBlank String bucketName) {
            try {
                InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName).build());
                return new InputStreamResource(inputStream);
            }catch (MinioException e) {
                log.error("MinIO 获取文件流失败, objectName: {}, bucketName: {}, error: {}", objectName, bucketName, e.getMessage());
                throw new FileException("获取文件流失败");
            } catch (Exception e) {
                log.error("获取文件流失败, objectName: {}, bucketName: {}, error: {}", objectName, bucketName, e.getMessage());
                throw new FileException("获取文件流失败");
            }
    }

    /**
     * 删除文件
     * @param objectName 文件对象名称
     * @param bucketName 存储桶名称
     * @return true: 删除成功, false: 删除失败
     */
    public Boolean removeFile(@NotBlank String objectName,@NotBlank String bucketName){
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName).build());
            return true;
        }catch (MinioException e) {
            log.error("MinIO 删除文件失败, objectName: {}, bucketName: {}, error: {}", objectName, bucketName, e.getMessage());
            throw new FileException("删除文件失败");
        } catch (Exception e) {
            log.error("删除文件失败, objectName: {}, bucketName: {}, error: {}", objectName, bucketName, e.getMessage());
            throw new FileException("删除文件失败");
        }
    }




}
