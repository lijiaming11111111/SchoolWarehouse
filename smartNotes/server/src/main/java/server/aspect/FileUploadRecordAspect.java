package server.aspect;

import com.smartNotes.context.FileUploadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 上传文件切面
 * 上传文件时将对应的文件ID存入线程空间
 */
@Aspect
@Component
public class FileUploadRecordAspect {

    @Around("@annotation(com.smartNotes.annotation.FileUpload)")
    public Object recordUploadedFile(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();  // 执行原本的上传方法
        // 上传方法返回值假设是 Long 文件ID
        if (result instanceof Long) {
            FileUploadContext.addFileId((Long) result);
        }else if (result instanceof List) {
            List<Long> list = (List<Long>) result;
            FileUploadContext.setFileIds(list);
        }

        return result;
    }
}
