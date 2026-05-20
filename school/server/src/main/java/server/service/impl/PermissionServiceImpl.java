package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.school.dto.permission.InsertPermissionDTO;
import com.school.dto.permission.UpdatePermissionDTO;
import com.school.entity.Permission;
import com.school.exception.BaseException;
import com.school.vo.permission.SelectPermissionIdVO;
import com.school.vo.permission.SelectPermissionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.mapper.PermissionMapper;
import server.service.PermissionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    public String insertPermission(InsertPermissionDTO insertPermissionDTO) {
        // 校验权限编码是否已存在
        SelectPermissionIdVO permissionVO = permissionMapper.selectPermissionCode(insertPermissionDTO.getCode());
        if (permissionVO != null) {
            throw new BaseException("权限编码已存在");
        }
        Permission permission = new Permission();
        permission.setId(String.valueOf(IdWorker.getId()));
        permission.setName(insertPermissionDTO.getName());
        permission.setCode(insertPermissionDTO.getCode());
        permission.setParentId(insertPermissionDTO.getParentId());
        permission.setPermissionType(insertPermissionDTO.getPermissionType());
        permissionMapper.insertPermission(permission);
        return permission.getId();
    }

    @Override
    public Boolean updatePermission(UpdatePermissionDTO updatePermissionDTO) {
        SelectPermissionIdVO permissionVO = permissionMapper.selectPermissionId(updatePermissionDTO.getId());
        if (permissionVO == null) {
            throw new BaseException("权限不存在");
        }
        Permission permission = new Permission();
        permission.setId(permissionVO.getId());
        permission.setName(updatePermissionDTO.getName());
        permission.setCode(updatePermissionDTO.getCode());
        permission.setParentId(updatePermissionDTO.getParentId());
        permission.setPermissionType(updatePermissionDTO.getPermissionType());
        permissionMapper.updatePermission(permission);
        return true;
    }

    @Override
    public Boolean deletePermission(String id) {
        SelectPermissionIdVO permissionVO = permissionMapper.selectPermissionId(id);
        if (permissionVO == null) {
            throw new BaseException("权限不存在");
        }
        permissionMapper.deletePermission(id);
        return true;
    }

    @Override
    public List<SelectPermissionVO> selectPermission() {
        // 获取所有权限
        List<SelectPermissionVO> permissionVOList = permissionMapper.selectPermission();

        Map<String, SelectPermissionVO> permissionMap = new HashMap<>();
        for (SelectPermissionVO permission : permissionVOList) {
            permissionMap.put(permission.getId(), permission);
        }

        // 构建权限树
        List<SelectPermissionVO> rootPermissions = new ArrayList<>();
        for (SelectPermissionVO permission : permissionVOList) {
            if ((permission.getParentId().equals("0"))) {
                // 顶级权限
                rootPermissions.add(permission);
            } else {
                // 子权限，添加到父权限的子列表中
                SelectPermissionVO parent = permissionMap.get(permission.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permission);
                }
            }
        }
        return rootPermissions;
    }

}
