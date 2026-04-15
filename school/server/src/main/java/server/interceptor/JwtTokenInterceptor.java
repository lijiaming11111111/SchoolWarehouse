package server.interceptor;

import com.school.bo.user.UserLoginData;
import com.school.context.BaseContext;
import com.school.enums.redis.RedisPrefix;
import com.school.exception.BaseException;
import com.school.result.Result;
import com.school.util.JwtUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import server.mapper.RoleMapper;
import server.mapper.UserMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 厉佳铭
 * jwt令牌校验的拦截器
 * 身份验证，权限验证
 */
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    private final RoleMapper roleMapper;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "token");
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        //1、从请求头中获取令牌
        String token = request.getHeader("token");
        //判断token是否为空
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.error("token为空")));
            response.getWriter().flush();
            return false;
        }
        try {
            //2、校验令牌
            //根据Key解密token
            Claims claims = JwtUtil.parseJWT(jwtSecretKey, token);
            Long userId = Long.valueOf(claims.get("id").toString());
            //将ID存入线程空间中
            BaseContext.setCurrentUserId(userId);
            String s = redisTemplate.opsForValue().get(RedisPrefix.USER_LOGIN_DATA.getPrefix() + userId);
            if (s == null){
                throw new BaseException("请重新登录");
            }
            UserLoginData userLoginData = objectMapper.readValue(s, UserLoginData.class);
            if (!token.equals(userLoginData.getToken())) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(objectMapper.writeValueAsString(Result.error("账号已在其他地方登陆")));
                response.getWriter().flush();
                return false;
            }

            // 获取角色ID
            String roleId = userLoginData.getRoleId();
            BaseContext.setCurrentUserRoleRoleId(roleId);
            String requestPath = request.getRequestURI();

//            // 白名单：基本用户操作直接放行
//            if (requestPath.contains("/user/getCurrentUserData") || requestPath.contains("/user/logout")) {
//                return true;
//            }

//            //  从数据库查询用户权限
//            try {
//                List<String> userPermissions = roleMapper.selectUserPermissions(String.valueOf(userId));
//                if (userPermissions != null && !userPermissions.isEmpty()) {
//                    // 将请求路径转换为权限码格式（如 /user/getCurrentUserData → user:getCurrentUserData）
//                    String permCode = requestPath.substring(1).replace("/", ":");
//                    // 检查用户是否有该权限
//                    if (userPermissions.contains(permCode)) {
//                        return true;
//                    }
//                }
//            } catch (Exception e) {
//                // 数据库查询失败，继续检查角色权限
//            }

            //  如果用户无权限，检查角色权限
            if (roleId != null && !roleId.isEmpty()) {
                try {
                    List<String> rolePermissions = roleMapper.selectRolePermissions(roleId);
                    if (rolePermissions != null && !rolePermissions.isEmpty()) {
                        // 将请求路径转换为权限码格式
                        String permCode = requestPath.substring(1).replace("/", ":");
                        // 检查角色是否有该权限
                        if (rolePermissions.contains(permCode)) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                   throw new BaseException("获取角色权限失败");
                }
            }

            // 未匹配到权限
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.error("无权限访问资源")));
            response.getWriter().flush();
            return false;
        } catch (Exception e) {
            if( e.getClass() == ExpiredJwtException.class ){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(objectMapper.writeValueAsString(Result.error("token过期")));
                response.getWriter().flush();
                return false;
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(Result.error("token无效")));
            response.getWriter().flush();
            return false;
        }
    }
}