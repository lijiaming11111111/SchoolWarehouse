package server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartNotes.entity.GroupMember;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
    /**
     * 加入分组
     * @param groupId 分组ID
     * @param userId 用户ID
     */
    @Insert("insert into group_member (group_id, user_id,role) values (#{groupId}, #{userId},3)")
    void joinGroupOwner(String groupId, String userId);

    /**
     * 批量加入分组
     * @param groupId 分组ID
     * @param userId 用户ID列表
     */
    void batchJoin(String groupId, List<String> userId);

    /**
     * 加入分组
     * @param groupMember 分组成员
     */
    @Insert("insert into group_member (id,group_id, user_id) values (#{id}, #{groupId}, #{userId})")
    void joinGroup(GroupMember groupMember);

    /**
     * 获取分组成员
     * @param groupId 分组ID
     * @param userId 用户ID
     * @return 分组成员
     */
    @Select("select * from group_member where group_id = #{groupId} and user_id = #{userId}")
    GroupMember getGroupMember(String groupId, String userId);
}
