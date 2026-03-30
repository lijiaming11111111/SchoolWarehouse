package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.smartNotes.annotation.CleanUpFilesOnError;
import com.smartNotes.context.BaseContext;
import com.smartNotes.entity.File;
import com.smartNotes.exception.file.FileException;
import com.smartNotes.util.FileLicenseUtil;
import com.smartNotes.util.MinioUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import server.mapper.FileMapper;
import server.service.FileService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileImpl implements FileService {

    private final FileMapper fileMapper;

    private final MinioUtil minioUtil;

    /**
     * 文件许可生成与验证工具类
     */
    private final FileLicenseUtil fileLicenseUtil;

    /**
     * Minio存储桶名称
     */
    @Value("${minio.bucket}")
    private String bucketName;
    @Override
    public Boolean removeFile(List<Long> ids) {
        List<File> files = fileMapper.getByIds(ids);
        fileMapper.deleteByIds(ids);
        for (File file : files) {
            minioUtil.removeFile(file.getObjectName(), file.getBucketName());
        }
        return true;
    }

    @Override
    @CleanUpFilesOnError
    public Long upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileException("文件为空");
        }
        String fileName = file.getOriginalFilename();
        String objectName = minioUtil.upload(file,fileName,bucketName);
        Long fileId = IdWorker.getId();
        File f = File.builder()
                .id(fileId)
                .fileName(fileName)
                .objectName(objectName)
                .uploadTime(LocalDateTime.now())
                .bucketName(bucketName).build();
        fileMapper.insertFile(f);
        return fileId;
    }



    @Override
    public ResponseEntity<Resource> getFileResponseEntity(Long fileId, String license, long expire) {
        Long currentUserId = BaseContext.getCurrentUserId(); // 应从Session、Token中提取实际用户ID
        try {
            if (!fileLicenseUtil.verifyLicense(fileId, currentUserId, expire, license)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            File file = getFile(fileId);
            if (file == null){
                throw new FileException("文件不存在");
            }
            return getFileResponseEntity(file);
        } catch (Exception e) {
            // 其他错误，返回500
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public File getFile(Long fileId) {
        return fileMapper.getById(fileId);
    }

    @Override
    public ResponseEntity<Resource> getFileResponseEntity(File file) throws UnsupportedEncodingException {
        Resource resource = minioUtil.getResource(file.getObjectName(), file.getBucketName());

        String fileName = file.getFileName();
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    @Override
    public Boolean removeFile(Long id) {
        File file = getFile(id);
        if (file == null){
            throw new FileException("文件不存在");
        }
        fileMapper.deleteById(id);
        minioUtil.removeFile(file.getObjectName(), file.getBucketName());
        return true;
    }
}
