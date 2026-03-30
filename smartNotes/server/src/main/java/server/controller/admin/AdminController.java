package server.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smartNotes.dto.user.AddUserDTO;
import com.smartNotes.dto.user.UserLoginDTO;
import com.smartNotes.result.Result;
import com.smartNotes.vo.user.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.service.AdminService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name = "管理员")
@Validated
public class AdminController {

    private final AdminService adminService;

//    @PostMapping("/addUser")
//    @Operation(summary = "添加用户")
//    @ApiOperationSupport(author = "燕怡明")
//    public Result<String> addUser(@Valid @RequestPart("dto") AddUserDTO dto,
//                                  @RequestPart(value = "photo",required = false)
//    MultipartFile photo) {
//        return Result.success("添加用户成功",adminService.addUser(dto));
//    }
}
