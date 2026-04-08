package server.service;

import com.school.dto.permission.InsertPermissionDTO;
import com.school.dto.permission.UpdatePermissionDTO;
import com.school.vo.permission.SelectPermissionVO;

import java.util.List;

public interface PermissionService  {
    String insertPermission(InsertPermissionDTO insertPermissionDTO);

    Boolean updatePermission(UpdatePermissionDTO updatePermissionDTO);

    Boolean deletePermission(String id);

    List<SelectPermissionVO> selectPermission();
}
