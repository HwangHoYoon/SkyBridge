package com.skybridge.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChatCard {
    @Schema(description = "image", example = "https://", name = "image")
    private String image;

    @Schema(description = "title", example = "수능완성", name = "title")
    private String title;

    @Schema(description = "subject", example = "국어영역", name = "subject")
    private String subject;

    @Schema(description = "teacher", example = "황호윤", name = "teacher")
    private String teacher;

    @Schema(description = "level", example = "초급", name = "level")
    private String level;

    @Schema(description = "target", example = "고3", name = "target")
    private String target;
}
