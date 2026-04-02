package server.config;

import com.smartNotes.enums.role.ApplyStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ApplyStatusConverter implements Converter<String, ApplyStatus> {
    @Override
    public ApplyStatus convert(String source) {
        // 把前端传的数字字符串转成枚举
        Integer code = Integer.parseInt(source);
        return ApplyStatus.getByCode(code);
    }
}
