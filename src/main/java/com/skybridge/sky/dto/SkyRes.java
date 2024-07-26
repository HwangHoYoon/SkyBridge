package com.skybridge.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SkyRes {

    @Schema(description = "planList", name = "planList")
    private List<SkyResultShortRes> planList;

    @Schema(description = "teacherImage", example = "선생님 이미지", name = "teacherImage")
    private String teacherImage;

    @Schema(description = "resultId", example = "1", name = "resultId")
    private Long resultId;
}
