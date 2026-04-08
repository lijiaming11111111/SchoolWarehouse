package server.service;

import com.school.dto.role.AssignRolePermDTO;
import com.school.dto.role.InsertRoleDTO;
import com.school.dto.role.PageSelectRoleDTO;
import com.school.dto.role.UpdateRoleDTO;
import com.school.entity.Role;
import com.school.result.PageResult;
import com.school.vo.role.PageSelectRoleVO;
import com.school.vo.role.SelectRoleIdVO;
import com.school.vo.role.SelectRolePermissionVO;

import java.util.List;

public interface RoleService {
    String insertRole(InsertRoleDTO insertRoleDTO);

    Boolean updateRole(UpdateRoleDTO updateRoleDTO);

    Boolean deleteRole(String id);

    SelectRoleIdVO selectRoleId(String id);

    PageResult<PageSelectRoleVO> pageSelectRole(PageSelectRoleDTO pageSelectRoleDTO);

    String assignRolePerm(AssignRolePermDTO assignRolePermDTO);

    SelectRolePermissionVO selectRolePermissionId(String id);
}
