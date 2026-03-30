package server.aspect;



import com.school.context.FileUploadContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import server.service.FileService;

import java.util.List;

/**
 * 文件清理切面
 * 当方法抛出异常时,获取上传的文件ID并删除这些文件
 */
@Aspect
@RequiredArgsConstructor
@Component
public class FileCleanUpAspect {

    private final FileService fileService;

    @Around("@annotation(com.school.annotation.CleanUpFilesOnError)")
    public Object handleFileCleanUp(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 发生异常时清理上传的文件
            List<Long> uploadedFileIds = FileUploadContext.getFileIds();
            if (!uploadedFileIds.isEmpty()){
                fileService.removeFile(uploadedFileIds);
            }
            throw e;
        } finally {
            FileUploadContext.clear();
        }
    }

}
