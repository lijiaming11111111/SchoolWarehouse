package server.mapper;

import com.school.entity.Permission;
import com.school.vo.permission.SelectPermissionIdVO;
import com.school.vo.permission.SelectPermissionVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Insert("INSERT INTO permission (id, name, code, parent_id, permission_type) " +
            "VALUES (#{id}, #{name}, #{code}, #{parentId}, #{permissionType})")
    void insertPermission(Permission permission);

    @Select("SELECT * FROM permission WHERE id = #{id}")
    SelectPermissionIdVO selectPermissionId(String id);

    void updatePermission(Permission permission);

    @Delete("DELETE FROM permission WHERE id = #{id}")
    void deletePermission(String id);

    @Select("SELECT * FROM permission")
    List<SelectPermissionVO> selectPermission();

    @Select("SELECT * FROM permission WHERE code = #{code}")
    SelectPermissionIdVO selectPermissionCode(String code);
}
