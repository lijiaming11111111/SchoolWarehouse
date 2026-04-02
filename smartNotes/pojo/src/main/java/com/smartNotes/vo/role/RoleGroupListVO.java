package com.smartNotes.vo.role;


import com.smartNotes.enums.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class RoleGroupListVO {

    @Schema(description = "角色组名称")
    private String groupName;

    @Schema(description = "角色组")
    private Role role;

    @Schema(description = "角色列表")
    private List<RoleDataVO> roleList;

}
