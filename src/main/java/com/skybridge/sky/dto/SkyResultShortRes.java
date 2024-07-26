package com.skybridge.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SkyResultShortRes {
    @Schema(description = "startDate", example = "시작일", name = "startDate")
    private String startDate;
    @Schema(description = "endDate", example = "종료일", name = "endDate")
    private String endDate;
    @Schema(description = "title", example = "제목", name = "title")
    private String title;
}
