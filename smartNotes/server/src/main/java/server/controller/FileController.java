package server.controller;

import com.smartNotes.exception.file.FileException;
import com.smartNotes.result.Result;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import server.service.FileService;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/file")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name = "文件管理")
@Validated
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @ApiOperationSupport(author = "燕怡明")
    public Result<Long> upload(@Parameter(description = "照片") @RequestPart(value = "photo",required = false)
                                   MultipartFile photo) {
        Long fileId = fileService.upload(photo);
        return Result.success("上传文件成功",fileId);
    }

    @GetMapping("/download")
    @Operation(summary = "下载文件")
    @ApiOperationSupport(author = "燕怡明")
    public ResponseEntity<Resource> downloadFile(@NotNull(message = "文件ID为空") @RequestParam Long fileId,
                                                 @NotNull(message = "许可为空") @RequestParam String license,
                                                 @NotNull(message = "过期时间为空") @RequestParam long expire) {
        // 1. 判断许可是否过期
        if (System.currentTimeMillis() > expire) {
            throw new FileException("许可已过期");
        }
        return fileService.getFileResponseEntity(fileId,license,expire);
    }

}
