package server.service;

import com.school.dto.user.UserLoginDTO;
import com.school.vo.role.menu.MenuVO;
import com.school.vo.user.CurrentUserDataVO;
import com.school.vo.user.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    /**
     * 用户登录
     *
     * @param dto      账号，密码
     * @param response
     * @return 用户信息，token
     */
    UserLoginVO login(UserLoginDTO dto, HttpServletResponse response) throws JsonProcessingException;

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
