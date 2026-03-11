package server.mapper;

import com.basketball.bo.user.UserLoginVerifyData;
import com.basketball.vo.user.CurrentUserDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from administrator where telephone=#{telephone}")
    UserLoginVerifyData getUserLoginDataByAccount(String telephone);

    /**
     * 获取当前用户信息
     * @param id 用户ID
     * @return 当前用户信息
     */
    @Select("select * from administrator where id=#{id}")
    CurrentUserDataVO getUserBasicDataById(Long id);
}
