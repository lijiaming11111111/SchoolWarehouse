package server.mapper;

import com.github.pagehelper.Page;
import com.smartNotes.dto.group.PageQueryGroupDTO;
import com.smartNotes.entity.GroupInfo;
import com.smartNotes.entity.GroupMember;
import com.smartNotes.enums.role.Role;
import com.smartNotes.vo.group.PageQueryGroupVO;
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

    @Insert("insert into group_info (id, group_name, owner_id,group_photo_id) " +
            "values (#{id}, #{groupName}, #{ownerId},#{groupPhotoId})")
    void createGroup(GroupInfo groupInfo);

    /**
     * 分页查询分组
     * @param queryDTO 查询页参数
     * @return 分页结果
     */
    Page<PageQueryGroupVO> pageQueryGroup(PageQueryGroupDTO queryDTO);


   }
