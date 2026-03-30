package server.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.school.exception.file.FileException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.service.FileService;

import javax.validation.constraints.NotNull;

/**
 * @Author: 厉佳铭
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@Tag(name = "文件")
@Validated
public class FileController {

    private final FileService fileService;

    @GetMapping("/download")
    @Operation(summary = "下载文件")
    @ApiOperationSupport(author = "厉佳铭")
    public ResponseEntity<Resource> downloadFile(@NotNull(message = "文件ID为空") @RequestParam Long fileId,
                                                 @NotNull(message = "许可为空") @RequestParam String license,
                                                 @NotNull(message = "过期时间为空") @RequestParam long expire) {
        if (System.currentTimeMillis() > expire) {
            throw new FileException("许可已过期");
        }
        return fileService.getFileResponseEntity(fileId,license,expire);
    }
}
