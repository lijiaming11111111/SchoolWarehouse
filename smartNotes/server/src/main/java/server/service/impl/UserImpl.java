package server.service.impl;

import com.basketball.bo.user.UserLoginData;
import com.basketball.bo.user.UserLoginVerifyData;
import com.basketball.context.BaseContext;
import com.basketball.dto.user.UserLoginDTO;
import com.basketball.enums.redis.RedisPrefix;
import com.basketball.exception.BaseException;
import com.basketball.exception.user.UserException;
import com.basketball.util.JwtUtil;
import com.basketball.vo.user.CurrentUserDataVO;
import com.basketball.vo.user.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import server.mapper.UserMapper;
import server.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UserImpl implements UserService {

    private final UserMapper userMapper;

    private final RedisTemplate<String,String> redisTemplate;

    private final ObjectMapper objectMapper;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Override
    public UserLoginVO login(UserLoginDTO dto, HttpServletResponse response) throws JsonProcessingException {
        // 1. 查询用户
        UserLoginVerifyData user = userMapper.getUserLoginDataByAccount(dto.getTelephone());
        if (user == null) {
            throw new UserException("用户不存在");
        }

        // 2. 校验密码
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new BaseException("密码错误");
        }

        // 3. 生成JWT
        long ttl = Boolean.TRUE.equals(dto.getRememberMe())
                ? 7 * 24 * 60 * 60 * 1000L
                : 60 * 60 * 1000L;

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        String token = JwtUtil.createJWT(jwtSecretKey, ttl, claims);

        UserLoginData userLoginData = new UserLoginData();
        userLoginData.setId(user.getId());
        userLoginData.setToken(token);

        redisTemplate.opsForValue().set(
                RedisPrefix.USER_LOGIN_DATA.getPrefix() + user.getId(),
                objectMapper.writeValueAsString(userLoginData),
                ttl,
                TimeUnit.MILLISECONDS
        );

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        if (Boolean.TRUE.equals(dto.getRememberMe())) {
            cookie.setMaxAge(7 * 24 * 60 * 60);
        } else {
            cookie.setMaxAge(-1);
        }

        response.addCookie(cookie);

        // 返回
        return UserLoginVO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .token(token)
                .build();
    }

    @Override
    public CurrentUserDataVO getCurrentUserData() {
        return userMapper.getUserBasicDataById(BaseContext.getCurrentUserId());
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = null;

        // 1. 从 Cookie 里获取 token
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            try {
                // 2. 解析 token 获取 userId
                Claims claims = JwtUtil.parseJWT(jwtSecretKey, token);
                Long userId = Long.parseLong(claims.get("id").toString());

                // 3. 删除 Redis 登录信息
            redisTemplate.delete(RedisPrefix.USER_LOGIN_DATA.getPrefix() + userId);
            } catch (Exception e) {
                // token 无效也没关系，继续清除 Cookie
            }
        }

        // ====================== 4. 清除浏览器 Cookie（核心） ======================
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 立即过期
        response.addCookie(cookie);
    }
}
