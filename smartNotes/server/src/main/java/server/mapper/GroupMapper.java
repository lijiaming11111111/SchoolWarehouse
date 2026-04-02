package server.mapper;

import com.smartNotes.entity.GroupInfo;
import com.smartNotes.entity.GroupMember;
import com.smartNotes.enums.role.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper {
    /**
     * 创建分组
     * @param groupInfo 分组信息
     * @return 分组ID
     */

    @Insert("insert into group_info (id, group_name, owner_id) " +
            "values (#{id}, #{groupName}, #{ownerId})")
    void createGroup(GroupInfo groupInfo);


   }
