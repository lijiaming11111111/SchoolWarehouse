package com.smartNotes.vo.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smartNotes.vo.file.FileDataVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PageQueryGroupVO {
    @Schema(description = "群组id")
    private String id;

    @Schema(description = "群组名称")
    private String groupName;

    @Schema(description = "群主 / 创建者的用户 ID")
    private String ownerId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "群聊头像文件")
    private FileDataVO photo;
}
