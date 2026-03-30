package com.school.util;

import com.school.exception.file.FileException;
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

    /**
     * 上传文件到MinIO
     * @param file 文件
     * @return 文件对象名称
     */
    public String upload(MultipartFile file,String fileName,String bucket) {
        //获取原始文件名
        String objectName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        //上传文件
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()).build());
            return objectName;
        } catch (Exception e) {
            throw new FileException("上传文件失败");
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
