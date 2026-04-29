package server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.school.annotation.CleanUpFilesOnError;
import com.school.annotation.FilePreSignature;
import com.school.bo.user.UserLoginData;
import com.school.bo.user.UserLoginVerifyData;
import com.school.context.BaseContext;
import com.school.dto.user.*;
import com.school.entity.User;
import com.school.enums.redis.RedisPrefix;
import com.school.enums.user.Gender;
import com.school.enums.user.StatusEnum;
import com.school.exception.BaseException;
import com.school.exception.user.UserException;
import com.school.result.PageResult;
import com.school.util.JwtUtil;
import com.school.util.SaltUtil;
import com.school.vo.item.SelectItemVO;
import com.school.vo.role.SelectRoleIdVO;
import com.school.vo.user.CurrentUserDataVO;
import com.school.vo.user.PageQueryUserVO;
import com.school.vo.user.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import server.mapper.RoleMapper;
import server.mapper.UserMapper;
import server.service.FileService;
import server.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final RedisTemplate<String,String> redisTemplate;

    private final ObjectMapper objectMapper;

    private  final FileService fileService;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    @Override
    public UserLoginVO login(UserLoginDTO dto, HttpServletResponse response) throws JsonProcessingException {
        // 查询用户（支持手机号或邮箱）
        UserLoginVerifyData user = userMapper.getUserLoginDataByAccount(dto.getAccount());
        if (user == null) {
            throw new UserException("账号不存在");
        }

        String encryptedPassword = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes());
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new BaseException("密码错误");
        }

        // 生成JWT
        long ttl = Boolean.TRUE.equals(dto.getRememberMe())
                ? 7 * 24 * 60 * 60 * 1000L
                : 60 * 60 * 1000L;

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        String token = JwtUtil.createJWT(jwtSecretKey, ttl, claims);

        UserLoginData userLoginData = new UserLoginData();
        userLoginData.setId(user.getId());
        userLoginData.setRoleId(user.getRoleId());
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

//        String roleName = roleMapper.selectRoleId(user.getRoleId()).getRoleName();
        String roleName = null;
        SelectRoleIdVO role = roleMapper.selectRoleId(user.getRoleId());
        if (role != null) {
            roleName = role.getRoleName();
        }
        // 返回
        return UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userAccount(user.getUserAccount())
                .role(roleName)
                .token(token)
                .build();
    }

    @Override
    @FilePreSignature
    public CurrentUserDataVO getCurrentUserData() {
        return userMapper.getUserBasicDataById(BaseContext.getCurrentUserId());
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = null;

        // 从 Cookie 里获取 token
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
                //  解析 token 获取 userId
                Claims claims = JwtUtil.parseJWT(jwtSecretKey, token);
                Long userId = Long.parseLong(claims.get("id").toString());

                //  删除 Redis 登录信息
            redisTemplate.delete(RedisPrefix.USER_LOGIN_DATA.getPrefix() + userId);
            } catch (Exception e) {
                // token 无效也没关系，继续清除 Cookie
            }
        }

        // 清除浏览器 Cookie（核心）
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 立即过期
        response.addCookie(cookie);
    }

    @Override
    @CleanUpFilesOnError
    @Transactional(rollbackFor = Exception.class)
    public String addUser(AddUserDTO addUserDTO, MultipartFile face) {
        // 验证手机号是否已存在
        Integer phoneCount = userMapper.selectUserByTelephone(addUserDTO.getTelephone());
        if (phoneCount != null && phoneCount > 0) {
            throw new BaseException("手机号已存在");
        }

        // 验证用户名是否已存在
        Integer userNameCount = userMapper.selectUserByUserName(addUserDTO.getUserName());
        if (userNameCount != null && userNameCount > 0) {
            throw new BaseException("用户名已存在");
        }

        // 验证账号是否已存在
        Integer userAccountCount = userMapper.selectUserByUserAccount(addUserDTO.getUserAccount());
        if (userAccountCount != null && userAccountCount > 0) {
            throw new BaseException("账号已存在");
        }

        // 验证邮箱是否已存在
        Integer emailCount = userMapper.selectUserByEmail(addUserDTO.getEmail());
        if (emailCount != null && emailCount > 0) {
            throw new BaseException("邮箱已存在");
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(addUserDTO, user);
        long userId = IdWorker.getId();
        user.setId(String.valueOf(userId));
        user.setUserName(user.getUserName());
        user.setStatusEnum(StatusEnum.NORMAL);
        // 设定默认排序为 0
        if(user.getSort() == null){
            user.setSort(0);
        }

        // 保存头像
        if(face != null){
            user.setImageAddress(fileService.upload(face));
        }

        // 生成加密密码
        user.setPassword(DigestUtils.md5DigestAsHex(addUserDTO.getPassword().getBytes()));
        if(userMapper.insertUser(user) != 1){
            throw new BaseException("新增用户失败");
        }

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteUser(DeleteUserDTO deleteUserDTO) {
        if (deleteUserDTO.getId().equals(BaseContext.getCurrentUserId())){
            throw new BaseException("不可以删除自己");
        }
        User user = userMapper.getUserById(deleteUserDTO.getId());
        if (user == null || user.getId() == null ){
            throw new UserException("用户不存在");
        }

        // 清除头像
        if(user.getImageAddress() != null){
            fileService.removeFile(user.getImageAddress());
        }
        if(userMapper.deleteUserById(deleteUserDTO.getId()) != 1){
            throw new BaseException("删除用户失败");
        }

        return true;
    }

    @Override
    @FilePreSignature
    public PageResult<PageQueryUserVO> pageQueryUser(PageQueryUserDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<PageQueryUserVO> page = userMapper.pageQueryUser(queryDTO);
        return new PageResult<>( page.getTotal(), page.getResult());
    }

    @Override
    @CleanUpFilesOnError
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUser(UpdateUserDTO updateUserDTO, MultipartFile face) {
        User user = userMapper.getUserById(Long.valueOf(updateUserDTO.getId()));
        if (user == null || user.getId() == null ){
            throw new UserException("用户不存在");
        }
        if (updateUserDTO.getTelephone() != null){
            // 验证手机号是否已存在（排除当前用户）
            if (!updateUserDTO.getTelephone().equals(user.getTelephone())) {
                Integer phoneCount = userMapper.selectUserByTelephone(updateUserDTO.getTelephone());
                if (phoneCount != null && phoneCount > 0) {
                    throw new BaseException("手机号已存在");
                }
            }
        }
        if (updateUserDTO.getUserName() != null){
            // 验证用户名是否已存在（排除当前用户）
            if (!updateUserDTO.getUserName().equals(user.getUserName())) {
                Integer userNameCount = userMapper.selectUserByUserName(updateUserDTO.getUserName());
                if (userNameCount != null && userNameCount > 0) {
                    throw new BaseException("用户名已存在");
                }
            }
        }
        if (updateUserDTO.getUserAccount() != null){
            // 验证账号是否已存在（排除当前用户）
            if (!updateUserDTO.getUserAccount().equals(user.getUserAccount())) {
                Integer userAccountCount = userMapper.selectUserByUserAccount(updateUserDTO.getUserAccount());
                if (userAccountCount != null && userAccountCount > 0) {
                    throw new BaseException("账号已存在");
                }
            }
        }
        if (updateUserDTO.getEmail() != null){
            // 验证邮箱是否已存在（排除当前用户）
            if (!updateUserDTO.getEmail().equals(user.getEmail())) {
                Integer emailCount = userMapper.selectUserByEmail(updateUserDTO.getEmail());
                if (emailCount != null && emailCount > 0) {
                    throw new BaseException("邮箱已存在");
                }
            }
        }
        User updateUser = new User();
        BeanUtils.copyProperties(updateUserDTO, updateUser);
        updateUser.setId(updateUserDTO.getId());
        // 修改头像
        if(face!= null){
            updateUser.setImageAddress(fileService.upload(face));
        }

        if(userMapper.updateUser(updateUser) != 1){
            throw new BaseException("修改用户失败");
        }

        if(face != null){
            // 获取原头像id
            Long oldFaceId = user.getImageAddress();
            if(oldFaceId != null){
                fileService.removeFile(oldFaceId);
            }
        }

        // 修改用户
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = userMapper.getUserById(resetPasswordDTO.getUserId());
        if(user == null || user.getId() == null ){
            throw new UserException("用户不存在");
        }
        // 重新生成密码
        String newPassword = resetPasswordDTO.getNewPassword();
        user.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));

        if(userMapper.updateUser(user) != 1){
            throw new BaseException("重置密码失败");
        }

        return true;
    }

    @Override
    public Boolean register(RegisterDTO registerDTO) {
        String key = "email:code:" + registerDTO.getEmail();
        String cacheCode = redisTemplate.opsForValue().get(key);
        if (cacheCode == null || !cacheCode.equals(registerDTO.getCode())) {
            throw new BaseException("验证码错误或已过期");
        }
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BaseException("两次输入密码不一致");
        }
        redisTemplate.delete(key);
        User user = new User();
        user.setId(String.valueOf(IdWorker.getId()));
        user.setGender(Gender.MAN);
        user.setPassword(DigestUtils.md5DigestAsHex(registerDTO.getPassword().getBytes()));
        user.setEmail(registerDTO.getEmail());
        user.setStatusEnum(StatusEnum.NORMAL);
        if(userMapper.insertUser(user) != 1){
            throw new BaseException("新增用户失败");
        }
        return true;
    }

    @Override
    public Boolean sendRegisterEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BaseException("邮箱不能为空");
        }
        String code = String.valueOf((int)((Math.random()*9+1)*100000));
        String key = "email:code:" + email;
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("设备管理系统注册验证码");
        message.setText("您的注册验证码是：" + code + "，5分钟内有效");
        mailSender.send(message);
        return true;
    }

}
