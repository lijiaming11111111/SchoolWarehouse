package server.service;

import com.smartNotes.dto.user.UserLoginDTO;
import com.smartNotes.vo.user.CurrentUserDataVO;
import com.smartNotes.vo.user.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface UserService {

    /**
     * 用户登录
     *
     * @param dto      账号，密码
     * @param response
     * @param session  会话对象
     * @return 用户信息，token
     */
    UserLoginVO login(UserLoginDTO dto, HttpServletResponse response, HttpSession session) throws JsonProcessingException;

    /**
     * 获取当前用户信息
     * @return 当前用户信息
     */
    CurrentUserDataVO getCurrentUserData();

    /**
     * 用户退出登录
     * @param request
     * @param response
     */
    void logout(HttpServletRequest request, HttpServletResponse response);

    /**
     * 用户注册
     * @param dto 注册信息
     * @param photo 头像
     * @return 注册成功后的用户ID
     */
    String register(com.smartNotes.dto.user.RegisterDTO dto, MultipartFile photo);
}
