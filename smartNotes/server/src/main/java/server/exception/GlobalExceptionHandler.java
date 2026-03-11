package server.exception;
import com.basketball.exception.BaseException;
import com.basketball.result.Result;
import com.basketball.vo.ValidationError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理
 */

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 处理参数验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Result<List<ValidationError>>> handleValidException(MethodArgumentNotValidException e){
        List<ValidationError> validationErrors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            ValidationError v = new ValidationError();
            v.setField(error.getField());
            v.setMessage(error.getDefaultMessage());
            validationErrors.add(v);
        }
        Result<List<ValidationError>> result = Result.error("字段验证错误",validationErrors);
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * 处理其他异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Result<Object>> handleException(Exception e) {
        String message = e.getMessage();
        Class<? extends Exception> eClass = e.getClass();
        log.error("发生异常:", e);
        if (eClass == NoHandlerFoundException.class){
            Result<Object> result = Result.error("请求的接口不存在");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
        //判断是否为请求类型异常
        if (eClass == HttpRequestMethodNotSupportedException.class) {
            Result<Object> result = Result.error("接口不支持的请求类型");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        //判断是否为业务异常
        if (e instanceof BaseException) {
            Result<Object> result = Result.error(message);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        if (env.equals("dev")){
            Result<Object> result = Result.error(e.toString());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        if (e instanceof HttpMessageNotReadableException) {
            Result<Object> result = Result.error("无法解析请求");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        Result<Object> result = Result.error("发生未知错误");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
