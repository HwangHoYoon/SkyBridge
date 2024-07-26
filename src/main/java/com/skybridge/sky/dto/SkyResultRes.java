package com.skybridge.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SkyResultRes {
    private Long resultId;
    private String intro;
    private String outro;
    @Schema(description = "planList", name = "planList")
    private List<SkyResultLongRes> planList;
}
