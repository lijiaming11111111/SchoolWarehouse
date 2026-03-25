package server.controller;

import com.smartNotes.dto.user.UserLoginDTO;
import com.smartNotes.result.Result;
import com.smartNotes.vo.user.CurrentUserDataVO;
import com.smartNotes.vo.user.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.interceptor.OnlineUserManager;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name = "用户")
@Validated
public class UserController {
    private final UserService userService;

    private final OnlineUserManager onlineUserManager;

    @PostMapping("/login")
    @Operation(summary = "登录")
    @ApiOperationSupport(author = "燕怡明")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO dto, HttpServletResponse response, HttpSession session)
            throws JsonProcessingException {
        return Result.success("登陆成功",userService.login(dto,response,session));
    }

    @GetMapping("/getCurrentUserData")
    @Operation(summary = "获取当前用户信息")
    @ApiOperationSupport(author = "燕怡明")
    public Result<CurrentUserDataVO> getCurrentUserData() {
        return Result.success("查询成功", userService.getCurrentUserData());
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    @ApiOperationSupport(author = "燕怡明")
    public Result<String> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        userService.logout(request, response);
        return Result.success("退出成功");
    }

    @GetMapping("/isOnline/{userId}")
    public boolean isOnline(@PathVariable String userId) {
        return onlineUserManager.isOnline(userId);
    }
}
