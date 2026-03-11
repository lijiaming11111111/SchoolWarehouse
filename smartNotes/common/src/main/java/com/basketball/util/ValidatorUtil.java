package com.basketball.util;


import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Set;


/**
 * 字段校验工具类
 */
@Component
@RequiredArgsConstructor
public class ValidatorUtil {

    private final Validator validator;


    public <T> void validate(T dto, Class<?>... groups) throws MethodArgumentNotValidException {

        //执行校验,返回错误结果
        Set<ConstraintViolation<T>> violations = validator.validate(dto, groups);

        //判断是否有校验错误
        if (!violations.isEmpty()) {

            //如果有错误,则将错误信息封装成BindingResult对象
            BindingResult bindingResult = new BeanPropertyBindingResult(dto, dto.getClass().getSimpleName());

            //将错误信息添加到BindingResult对象中
            for (ConstraintViolation<T> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                bindingResult.addError(new FieldError(dto.getClass().getSimpleName(), field, message));
            }
            //抛出MethodArgumentNotValidException异常
            throw new MethodArgumentNotValidException(getMethodParameter(), bindingResult);
        }

    }

    // 假方法,用于防止springboot抛出异常
    private MethodParameter getMethodParameter() {
        try {
            Method method = DummyController.class.getDeclaredMethod("dummy", Object.class);
            return new MethodParameter(method, 0);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // 假Controller
    private static class DummyController {
        public void dummy(Object obj) {}
    }
}
