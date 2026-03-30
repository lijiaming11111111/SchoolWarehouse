package server.mapper;

import com.github.pagehelper.Page;
import com.school.annotation.AutoFill;
import com.school.bo.user.UserLoginVerifyData;
import com.school.dto.user.PageQueryUserDTO;
import com.school.entity.User;
import com.school.vo.user.CurrentUserDataVO;
import com.school.vo.user.PageQueryUserVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT id, user_name,user_account, password FROM user WHERE telephone = #{account} OR email = #{account}")
    UserLoginVerifyData getUserLoginDataByAccount(String telephone);

    /**
     * 获取当前用户信息
     * @param id 用户ID
     * @return 当前用户信息
     */
    CurrentUserDataVO getUserBasicDataById(Long id);

    /**
     * 手机号查询用户
     */
    @Select("SELECT COUNT(id) FROM user WHERE telephone = #{telephone}")
    Integer selectUserByTelephone(String telephone);

    /**
     * 用户名查询用户
     */
    @Select("SELECT COUNT(id) FROM user WHERE user_name = #{userName}")
    Integer selectUserByUserName(String userName);

    /**
     * 新增用户
     *
     * @param user 需要新增的用户信息
     * @return 新增结果
     */
    @Insert("INSERT INTO user (id,user_name, user_account,gender, telephone, password, " +
            " email, status_enum,image_address, department_id) " +
            "VALUES (#{id}, #{userName}, #{userAccount}, #{gender}, #{telephone}, #{password}, " +
            " #{email}, #{statusEnum}, #{imageAddress}, #{departmentId})")
    @AutoFill(AutoFill.OperationType.INSERT)
    int insertUser(User user);

    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    @Select("select * from user where id=#{id}")
    User getUserById(Long userId);

    /**
     * 根据ID删除用户
     *
     * @param id 需要删除的用户ID
     * @return 删除结果
     */
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUserById(Long id);

    Page<PageQueryUserVO> pageQueryUser(PageQueryUserDTO queryDTO);

    int updateUser(User updateUser);

    /**
     * 账号查询用户
     */
    @Select("SELECT COUNT(id) FROM user WHERE user_account = #{userAccount}")
    Integer selectUserByUserAccount(String userAccount);

    /**
     * 邮箱查询用户
     */
    @Select("SELECT COUNT(id) FROM user WHERE email = #{email}")
    Integer selectUserByEmail(String email);

}
