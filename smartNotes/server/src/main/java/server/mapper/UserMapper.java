package server.mapper;

import com.smartNotes.bo.user.UserLoginVerifyData;
import com.smartNotes.entity.User;
import com.smartNotes.vo.user.CurrentUserDataVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where telephone=#{telephone}")
    UserLoginVerifyData getUserLoginDataByAccount(String telephone);

    /**
     * 获取当前用户信息
     * @param id 用户ID
     * @return 当前用户信息
     */

    CurrentUserDataVO getUserBasicDataById(Long id);

    @Select("select * from user where telephone=#{telephone}")
    User getUserByTelephone(String telephone);

    /**
     * 注册用户
     * @param user 用户信息
     */
    @Insert("insert into user (id, name, gender, telephone, password, photo_id) " +
            "values(#{id},#{name},#{gender},#{telephone},#{password},#{photoId})")
    void register(User user);
}
