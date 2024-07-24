package com.skybridge.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SkyRes {
    @Schema(description = "planText", example = "계획", name = "planText")
    private String planText;

    @Schema(description = "teacherImage", example = "선생님 이미지", name = "teacherImage")
    private String teacherImage;

    @Schema(description = "subject", example = "과목", name = "subject")
    private String subject;
}
