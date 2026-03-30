package server.controller;

import cn.hutool.core.bean.BeanUtil;
import com.school.dto.user.*;
import com.school.result.PageResult;
import com.school.result.Result;
import com.school.vo.user.CurrentUserDataVO;
import com.school.vo.user.PageQueryUserVO;
import com.school.vo.user.UserLoginVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    @ApiOperationSupport(author = "厉佳铭")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO dto, HttpServletResponse response) throws JsonProcessingException {
        return Result.success("登陆成功",userService.login(dto,response));
    }

    @GetMapping("/getCurrentUserData")
    @Operation(summary = "获取当前用户信息")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<CurrentUserDataVO> getCurrentUserData() {
        return Result.success("查询成功", userService.getCurrentUserData());
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<String> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return Result.success("退出成功");
    }

    @PostMapping("/addUser")
    @Operation(summary = "新增用户",description = "入参1.data:用户信息JSON格式;" + "2. face:用户头像图片;")
    @ApiOperationSupport(author = "厉佳铭")
    public Result addUser(@Valid  @RequestPart("data") AddUserDTO addUserDTO ,
                          @RequestPart(value = "face", required = false) MultipartFile face) {
        return Result.success("新增成功", userService.addUser(addUserDTO, face));
    }

    @PostMapping("/deleteUser")
    @Operation(summary = "删除用户")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> deleteUser(@Valid @RequestBody DeleteUserDTO deleteUserDTO)  {
        return Result.success("删除成功",userService.deleteUser(deleteUserDTO));
    }

    @PostMapping("/pageQueryUser")
    @Operation(summary = "分页查询用户")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<PageResult<PageQueryUserVO>> pageQueryUser(@Valid @RequestBody PageQueryUserDTO queryDTO) {
        PageResult<PageQueryUserVO> pageResult = userService.pageQueryUser(queryDTO);
        return Result.success("查询成功", pageResult);
    }

    @PostMapping("/updateUser")
    @Operation(summary = "修改用户", description = "入参1.data:用户信息JSON格式;" + "2. face:用户头像图片;")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> updateUser(@Valid @RequestPart("data") UpdateUserDTO updateUserDto,
                                      @RequestPart(value = "face", required = false) MultipartFile face) {
        if(BeanUtil.isEmpty(updateUserDto,"id") && face == null) {
            return Result.error("未传入需要修改的内容");
        }
        return Result.success("修改成功", userService.updateUser(updateUserDto, face));
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "重置密码")
    @ApiOperationSupport(author = "厉佳铭")
    public Result<Boolean> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO)  {
        return Result.success("重置密码成功", userService.resetPassword(resetPasswordDTO));
    }

}
