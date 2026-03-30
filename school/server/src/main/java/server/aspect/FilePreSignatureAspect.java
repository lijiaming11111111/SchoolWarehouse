package server.aspect;


import com.school.context.BaseContext;
import com.school.util.FileLicenseUtil;
import com.school.vo.file.FileDataVO;
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


    @Around("@annotation(com.school.annotation.FilePreSignature)")
    public Object addPreSignature(ProceedingJoinPoint joinPoint) throws Throwable {
        // 执行目标方法
        Object result = joinPoint.proceed();

        // 递归处理对象中的 FileDataVO 字段
        if (result != null) {
            processFileDataVO(result, Collections.newSetFromMap(new IdentityHashMap<>()));
        }

        return result;
    }

    private void processFileDataVO(Object obj, Set<Object> visited) throws Exception {
        // 判断是否为 null、基本类型/包装类/字符串，或已访问过
        if (obj == null || isPrimitiveOrWrapperOrString(obj.getClass()) || visited.contains(obj)) {
            return;
        }
        visited.add(obj);

        // 判断是否为 FileDataVO
        if (obj instanceof FileDataVO) {
            FileDataVO fileData = (FileDataVO) obj;
            if (fileData.getId() != null) {
                fileData.setUrl(licenseUtil.generateSignedUrl(fileData.getId(), BaseContext.getCurrentUserId()));
            }
            return;
        }

        // List 支持
        if (obj instanceof List<?>) {
            for (Object item : (List<?>) obj) {
                processFileDataVO(item, visited);
            }
            return;
        }

        // Map 支持
        if (obj instanceof Map<?, ?>) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                processFileDataVO(entry.getValue(), visited);
            }
            return;
        }

        // 关键修复：处理 PageResult 类
        if (obj.getClass().getSimpleName().equals("PageResult")) {
            try {
                Field resultField = obj.getClass().getDeclaredField("records");
                resultField.setAccessible(true);
                Object resultValue = resultField.get(obj);
                if (resultValue != null) {
                    processFileDataVO(resultValue, visited);
                }
            } catch (Exception e) {
                // 忽略异常，继续处理其他字段
            }
        }

        // 关键修复：跳过 JDK 内部类，只处理自己业务包下的对象
        Class<?> clazz = obj.getClass();
        if (clazz.getName().startsWith("java.") || clazz.getName().startsWith("javax.")) {
            return;
        }

        // 是其他对象，递归处理其字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 跳过静态字段
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // 跳过 JDK 内部类类型的字段
            Class<?> fieldType = field.getType();
            if (fieldType.getName().startsWith("java.") || fieldType.getName().startsWith("javax.")) {
                continue;
            }

            try {
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                if (fieldValue != null) {
                    processFileDataVO(fieldValue, visited);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
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
