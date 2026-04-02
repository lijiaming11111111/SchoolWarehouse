package server.service;

import com.smartNotes.dto.group.CreateGroupDTO;
import com.smartNotes.entity.GroupApply;
import com.smartNotes.enums.role.ApplyStatus;

import java.util.List;

public interface GroupService {
    String createGroup(CreateGroupDTO createGroupDTO);

    Boolean joinGroup(String groupId, String userId);

    Boolean applyJoinGroup(String groupId);

    List<GroupApply> getCheckApplies();

    List<GroupApply> getMyApplys();

    Boolean auditApply(String applyId, ApplyStatus applyStatus);
}
