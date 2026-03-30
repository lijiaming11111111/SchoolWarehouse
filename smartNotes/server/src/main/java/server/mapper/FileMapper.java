package server.mapper;


import com.smartNotes.entity.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    /**
     * 新增文件记录
     * @param f 文件对象
     * @return 结果
     */
    @Insert("INSERT INTO file (id, file_name, object_name, bucket_name, upload_time) VALUES (#{id}, #{fileName}, " +
            "#{objectName}, #{bucketName}, #{uploadTime})")
    Integer insertFile(File f);

    /**
     * 根据ID查询文件
     * @param id 文件ID
     * @return 文件信息
     */
    @Select("SELECT * FROm file WHERE id = #{id}")
    File getById(Long id);

    /**
     * 根据ID删除文件
     * @param id 文件ID
     * @return 结果
     */
    @Delete("DELETE FROM file WHERE id = #{id}")
    Integer deleteById(Long id);

    /**
     * 批量查询文件
     * @param ids 文件ID列表
     * @return 文件列表
     */
    List<File> getByIds(List<Long> ids);

    /**
     * 批量删除文件
     * @param ids 文件ID列表
     * @return 结果
     */
    Integer deleteByIds(List<Long> ids);

}
