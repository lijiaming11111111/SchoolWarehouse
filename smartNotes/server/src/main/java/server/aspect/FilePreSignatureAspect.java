package server.aspect;


import com.smartNotes.context.BaseContext;
import com.smartNotes.util.FileLicenseUtil;
import com.smartNotes.vo.file.FileDataVO;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;


/**
 * 文件预签名切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class FilePreSignatureAspect {

    // 文件预签名工具类
    private final FileLicenseUtil licenseUtil;


    @Around("@annotation(com.smartNotes.annotation.FilePreSignature)")
    public Object addPreSignature(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = joinPoint.proceed();

        // 处理 FileDataVO 字段
        if (result != null) {
            processFileDataVO(result, Collections.newSetFromMap(new IdentityHashMap<>()));
        }

        return result;
    }

    private void processFileDataVO(Object obj, Set<Object> visited) throws Exception {
        // 空值直接返回
        if (obj == null) return;


        if (visited.contains(obj)) return;
        visited.add(obj);

        //  遇到 FileDataVO
        if (obj instanceof FileDataVO) {
            FileDataVO vo = (FileDataVO) obj;
            if (vo.getId() != null) {
                String url = licenseUtil.generateSignedUrl(vo.getId(), BaseContext.getCurrentUserId());
                vo.setUrl(url);
            }
            return;
        }


        if (obj instanceof List<?>) {
            for (Object item : (List<?>) obj) {
                processFileDataVO(item, visited);
            }
            return;
        }

        // 处理数组
        if (obj.getClass().isArray()) {
            Object[] array = (Object[]) obj;
            for (Object o : array) {
                processFileDataVO(o, visited);
            }
            return;
        }

        // 6. 处理Map
        if (obj instanceof Map<?, ?>) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                processFileDataVO(entry.getValue(), visited);
            }
            return;
        }


        Class<?> clazz = obj.getClass();
        String className = clazz.getName();
        if (className.startsWith("java.") || className.startsWith("javax.") || className.startsWith("sun.")) {
            return;
        }

        // 8. 遍历所有字段
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                processFileDataVO(fieldValue, visited);
            } catch (Exception ignored) {}
        }
    }

    /**
     * 判断是否是基本数据类型、包装类或字符串
     * @param clazz 类对象
     * @return 是否
     */
    private boolean isPrimitiveOrWrapperOrString(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                Number.class.isAssignableFrom(clazz) ||
                Boolean.class.isAssignableFrom(clazz) ||
                Character.class.isAssignableFrom(clazz) ||
                Enum.class.isAssignableFrom(clazz);
    }
}
