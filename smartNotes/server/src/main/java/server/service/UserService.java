package server.service;

import com.basketball.dto.user.UserLoginDTO;
import com.basketball.vo.user.CurrentUserDataVO;
import com.basketball.vo.user.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;

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
}
