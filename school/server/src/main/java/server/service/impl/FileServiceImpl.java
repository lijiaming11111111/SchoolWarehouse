package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.school.annotation.FileUpload;
import com.school.context.BaseContext;
import com.school.entity.File;
import com.school.exception.file.FileException;
import com.school.util.FileLicenseUtil;
import com.school.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import server.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.mapper.FileMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

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

    /**
     * 上传单个文件
     * @param file 需要上传的文件
     * @return 文件ID
     */
    @Override
    @FileUpload
    @Transactional(rollbackFor = Exception.class)
    public Long upload(MultipartFile file){
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

    /**
     * 批量上传文件
     * @param files 需要上传的文件
     * @return 文件ID列表
     */
    @Override
    @FileUpload
    @Transactional(rollbackFor = Exception.class)
    public List<Long> upload(MultipartFile[] files) {
        List<Long> fileIds = new ArrayList<>();
        for (MultipartFile file : files) {
            fileIds.add(upload(file));
        }
        return fileIds;
    }


    /**
     * 获取文件信息
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    @Override
    public File getFile(Long fileId) {
        return fileMapper.getById(fileId);
    }

    /**
     * 删除单个文件
     *
     * @param id 需要删除的文件ID
     * @return 是否删除成功
     */
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

    /**
     * 删除文件
     *
     * @param ids 需要删除的文件ID列表
     * @return 是否删除成功
     */
    @Override
    public Boolean removeFile(List<Long> ids) {
        List<File> files = fileMapper.getByIds(ids);
        fileMapper.deleteByIds(ids);
        for (File file : files) {
            minioUtil.removeFile(file.getObjectName(), file.getBucketName());
        }
        return true;
    }

    /**
     * 获取文件资源
     *
     * @param file 文件ID
     * @return 文件资源
     */
    @Override
    public Resource getFileResource(File file) {
        return minioUtil.getResource(file.getObjectName(),file.getBucketName());
    }

    /**
     * 获取文件资源
     *
     * @param id 文件ID
     * @return 文件资源
     */
    @Override
    public Resource getFileResource(Long id) {
        File file = getFile(id);
        if (file == null){
            throw new FileException("文件不存在");
        }
        return getFileResource(file);
    }

    /**
     * 获取文件相应实体
     *
     * @param file 文件
     * @return 返回结果
     */
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

    /**
     * 获取文件相应实体
     *
     * @param id 文件ID
     * @return 返回结果
     */
    @Override
    public ResponseEntity<Resource> getFileResponseEntity(Long id) throws UnsupportedEncodingException {
        File file = getFile(id);
        if (file == null){
            throw new FileException("文件不存在");
        }
        return getFileResponseEntity(file);
    }


    /**
     * 生成预签名URL
     * @param fileId 文件ID
     * @param userId 用户ID
     * @return 预签名URL
     */
    private String generateSignedUrl(Long fileId, Long userId) {
        try {
            return fileLicenseUtil.generateSignedUrl(fileId, userId);
        } catch (Exception e) {
            throw new FileException("生成预签名URL失败");
        }
    }

    /**
     * 获取文件相应实体
     *
     * @param fileId  文件ID
     * @param license 许可
     * @param expire  过期时间
     * @return 返回结果
     */
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

}
