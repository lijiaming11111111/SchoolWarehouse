package server.service;


import com.smartNotes.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface FileService {
    /**
     * 删除文件
     * @param ids 需要删除的文件ID列表
     * @return 是否删除成功
     */
    Boolean removeFile(List<Long> ids);

    /**
     * 上传单个文件
     * @param file 需要上传的文件
     * @return 文件ID
     */
    Long upload(MultipartFile file);


    /**
     * 获取文件相应实体
     * @param fileId 文件ID
     * @param license 许可
     * @param expire 过期时间
     * @return 返回结果
     */
    ResponseEntity<Resource> getFileResponseEntity(Long fileId, String license, long expire);

    /**
     * 获取文件信息
     * @param fileId 文件ID
     * @return 文件信息
     */
    File getFile(Long fileId);

    /**
     * 获取文件相应实体
     * @param file 文件
     * @return 返回结果
     */
    ResponseEntity<Resource> getFileResponseEntity(File file) throws UnsupportedEncodingException;

    /**
     * 删除单个文件
     * @param id 需要删除的文件ID
     * @return 是否删除成功
     */
    Boolean removeFile(Long id);
}
