package server.service;

import com.smartNotes.dto.group.CreateGroupDTO;
import com.smartNotes.dto.group.PageQueryGroupDTO;
import com.smartNotes.entity.GroupApply;
import com.smartNotes.enums.role.ApplyStatus;
import com.smartNotes.result.PageResult;
import com.smartNotes.vo.group.PageQueryGroupVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GroupService {
    String createGroup(CreateGroupDTO createGroupDTO, MultipartFile photo);

    Boolean joinGroup(String groupId, String userId);

    Boolean applyJoinGroup(String groupId);

    List<GroupApply> getCheckApplies();

    List<GroupApply> getMyApplys();

    Boolean auditApply(String applyId, ApplyStatus applyStatus);

    Boolean kickMember(String groupId, String targetUserId);

    Boolean manageAdmin(String groupId, String targetUserId, Integer type);

    Boolean sendGroupMessage(String groupId, String content, MultipartFile file);

    PageResult<PageQueryGroupVO> pageQueryGroup(PageQueryGroupDTO queryDTO);
}
