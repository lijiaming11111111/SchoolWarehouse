package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.dto.role.*;
import com.school.entity.Role;
import com.school.entity.UserRole;
import com.school.exception.BaseException;
import com.school.result.PageResult;
import com.school.vo.role.PageSelectRoleVO;
import com.school.vo.role.SelectRoleIdVO;
import com.school.vo.role.SelectRolePermissionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.RoleMapper;
import server.mapper.UserMapper;
import server.service.RoleService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    private final UserMapper userMapper;

    @Override
    public String insertRole(InsertRoleDTO insertRoleDTO) {
        Role role = new Role();
        role.setId(String.valueOf(IdWorker.getId()));
        role.setRoleName(insertRoleDTO.getRoleName());
        role.setRoleCode(insertRoleDTO.getRoleCode());
        role.setStatusEnum(insertRoleDTO.getStatusEnum());
        roleMapper.insertRole(role);
        return role.getId();
    }

    @Override
    public Boolean updateRole(UpdateRoleDTO updateRoleDTO) {
        SelectRoleIdVO roleId = roleMapper.selectRoleId(String.valueOf(updateRoleDTO.getId()));
        if (roleId == null) {
            throw new BaseException("角色不存在");
        }
        if (updateRoleDTO.getId().equals("1")) {
            throw new BaseException("管理员不能修改");
        }
        Role role = new Role();
        role.setId(String.valueOf(updateRoleDTO.getId()));
        role.setRoleName(updateRoleDTO.getRoleName());
        role.setRoleCode(updateRoleDTO.getRoleCode());
        role.setStatusEnum(updateRoleDTO.getStatusEnum());
        roleMapper.updateRole(role);
        return true;
    }

    @Override
    public Boolean deleteRole(String id) {
        SelectRoleIdVO role = roleMapper.selectRoleId(id);
        if (role == null) {
            throw new BaseException("角色不存在");
        }
        roleMapper.deleteRoleId(id);
        return true;
    }

    @Override
    public SelectRoleIdVO selectRoleId(String id) {
        if(roleMapper.selectRoleId(id)==null){
            throw new BaseException("角色不存在");
        }
        return roleMapper.selectRoleId(id);
    }

    @Override
    public PageResult<PageSelectRoleVO> pageSelectRole(PageSelectRoleDTO pageSelectRoleDTO) {
        PageHelper.startPage(pageSelectRoleDTO.getPage(), pageSelectRoleDTO.getPageSize());
        Page<PageSelectRoleVO> page = roleMapper.pageSelectRole(pageSelectRoleDTO);
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    @Override
    public String assignRolePerm(AssignRolePermDTO assignRolePermDTO) {
        if (roleMapper.selectRoleId(assignRolePermDTO.getId())==null) {
            throw new BaseException("角色不存在");
        }
        // 处理添加权限的验证
        if (assignRolePermDTO.getAddPermissionIdList()!=null && !assignRolePermDTO.getAddPermissionIdList().isEmpty()){
            List<String> addPermissionIdList = roleMapper.selectAddPermissionIdList(assignRolePermDTO.getId(),assignRolePermDTO.getAddPermissionIdList());
            if (addPermissionIdList.size()!=assignRolePermDTO.getAddPermissionIdList().size()){
                throw new BaseException("添加的权限不存在");
            }

            List<String> existingPermissions = roleMapper.selectExistingPermissions(assignRolePermDTO.getId(), assignRolePermDTO.getAddPermissionIdList());
            if (!existingPermissions.isEmpty()) {
                throw new BaseException("有添加重复权限,权限是:" + String.join(", ", existingPermissions));
            }
            // 为每个权限生成唯一ID
            List<String> ids = new ArrayList<>();
            for (int i = 0; i < assignRolePermDTO.getAddPermissionIdList().size(); i++) {
                ids.add(String.valueOf(IdWorker.getId()));
            }
            roleMapper.addRolePermission(assignRolePermDTO.getId(), assignRolePermDTO.getAddPermissionIdList(), ids);
        }

        // 处理删除权限的验证
        if (assignRolePermDTO.getDeletePermissionIdList()!=null && !assignRolePermDTO.getDeletePermissionIdList().isEmpty()){
            List<String> deletePermissionIdList = roleMapper.selectDeletePermissionIdList(assignRolePermDTO.getId(),assignRolePermDTO.getDeletePermissionIdList());
            if (deletePermissionIdList.size()!=assignRolePermDTO.getDeletePermissionIdList().size()){
                throw new BaseException("删除的权限不存在");
            }

            roleMapper.deleteRolePermission(assignRolePermDTO.getId(),assignRolePermDTO.getDeletePermissionIdList());
        }
        return null;
    }

    @Override
    public SelectRolePermissionVO selectRolePermissionId(String id) {
        return roleMapper.selectRolePermissionId(id);
    }

    @Override
    public Boolean updateRoleUser(UpdateRoleUserDTO updateRoleUserDTO) {
        if (userMapper.getUserById(Long.valueOf(updateRoleUserDTO.getId()))==null){
            throw new BaseException("用户不存在");
        }
        if (roleMapper.selectRoleId(updateRoleUserDTO.getRoleId())==null) {
            throw new BaseException("角色不存在");
        }
        UserRole userRole = new UserRole();
        userRole.setId(String.valueOf(IdWorker.getId()));
        userRole.setRoleId(updateRoleUserDTO.getRoleId());
        userRole.setUserId(updateRoleUserDTO.getId());
        roleMapper.updateRoleUser(userRole);
        return true;
    }

    @Override
    public Boolean insertRoleUser(InsertRoleUserDTO insertRoleUserDTO) {
        if (userMapper.getUserById(Long.valueOf(insertRoleUserDTO.getId()))==null){
            throw new BaseException("用户不存在");
        }
        if (roleMapper.selectRoleId(insertRoleUserDTO.getRoleId())==null) {
            throw new BaseException("角色不存在");
        }
        try {
            UserRole userRole = new UserRole();
            userRole.setId(String.valueOf(IdWorker.getId()));
            userRole.setRoleId(insertRoleUserDTO.getRoleId());
            userRole.setUserId(insertRoleUserDTO.getId());
            roleMapper.insertRoleUser(userRole);
        }catch (Exception e){
            throw new BaseException("当前用户已存在该角色");
        }
        return true;
    }

}
