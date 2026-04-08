package server.mapper;

import com.github.pagehelper.Page;
import com.school.dto.role.PageSelectRoleDTO;
import com.school.entity.Role;
import com.school.vo.role.PageSelectRoleVO;
import com.school.vo.role.SelectRoleIdVO;
import com.school.vo.role.SelectRolePermissionVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Insert("INSERT INTO role (id, role_name, role_code, status_enum)" +
            " VALUES (#{id}, #{roleName}, #{roleCode}, #{statusEnum})")
    void insertRole(Role role);

    void updateRole(Role role);

    @Select("SELECT * FROM role WHERE id = #{id}")
    SelectRoleIdVO selectRoleId(String id);

    @Delete("DELETE FROM role WHERE id = #{id}")
    void deleteRoleId(String id);

    Page<PageSelectRoleVO> pageSelectRole(PageSelectRoleDTO pageSelectRoleDTO);

    void addRolePermission(@Param("id") String id, @Param("roleId") String roleId, @Param("addPermissionIdList") List<String> addPermissionIdList);

    void deleteRolePermission(@Param("id") String id, @Param("deletePermissionIdList") List<String> deletePermissionIdList);

    List<String> selectAddPermissionIdList(@Param("id") String id, @Param("addPermissionIdList") List<String> addPermissionIdList);

    List<String> selectDeletePermissionIdList(@Param("id") String id, @Param("deletePermissionIdList") List<String> deletePermissionIdList);

    List<String> selectExistingPermissions(@Param("id") String id, @Param("addPermissionIdList") List<String> addPermissionIdList);

    SelectRolePermissionVO selectRolePermissionId(String id);

    @Select("SELECT p.code FROM permission p " +
            "JOIN role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<String> selectRolePermissions(String roleId);
//    @Select("SELECT p.code FROM permission p " +
//            "JOIN role_permission rp ON p.id = rp.permission_id " +
//            "JOIN user_role ur ON rp.role_id = ur.role_id " +
//            "WHERE ur.user_id = #{userId}")
//    List<String> selectUserPermissions(String userId);
}
