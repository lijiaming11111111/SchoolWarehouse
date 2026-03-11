package server.controller;

import com.basketball.dto.user.UserLoginDTO;
import com.basketball.result.Result;
import com.basketball.vo.user.CurrentUserDataVO;
import com.basketball.vo.user.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/login")
    @Operation(summary = "登录")
    @ApiOperationSupport(author = "燕怡明")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO dto, HttpServletResponse response) throws JsonProcessingException {
        return Result.success("登陆成功",userService.login(dto,response));
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
}
