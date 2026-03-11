package server.interceptor;

import com.basketball.enums.redis.RedisPrefix;
import com.basketball.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

public class LoginInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String jwtSecretKey;

    public LoginInterceptor(RedisTemplate<String, Object> redisTemplate, String jwtSecretKey) {
        this.redisTemplate = redisTemplate;
        this.jwtSecretKey = jwtSecretKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 1. 从 Cookie 中获取 token
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 2. 没有 token → 未登录
        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            return false;
        }

        // 3. 解析 token
        Claims claims = JwtUtil.parseJWT(jwtSecretKey, token);
        Long userId = Long.parseLong(claims.get("id").toString());

        // 4. 校验 Redis 是否存在
        String redisToken = (String) redisTemplate.opsForValue().get(RedisPrefix.USER_LOGIN_DATA.getPrefix() + userId);
        if (redisToken == null || !redisToken.equals(token)) {
            response.setStatus(401);
            return false;
        }

        // 5. 把用户信息存入 request，供后续使用
        request.setAttribute("userId", userId);
        return true;
    }
}