package com.smartNotes.dto.group;

import com.smartNotes.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PageQueryGroupDTO {
    @Schema(description = "群组名称")
    private String groupName;

    @Schema(description = "页码", defaultValue = "1",required = true)
    @NotNull(message = "页码为空")
    private Integer page;

    @Schema(description = "每页显示记录数", defaultValue = "10",required = true)
    @NotNull(message = "页大小为空")
    @Min(value = 1,message = "页大小不能小于1")
    @Max(value = 50,message = "页大小不能超过50")
    private Integer pageSize;
}
