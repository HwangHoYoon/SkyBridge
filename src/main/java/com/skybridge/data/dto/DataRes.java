package com.skybridge.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DataRes {
    @Schema(description = "id", example = "id", name = "id")
    private Long id;

    @Schema(description = "dataName", example = "주요 상위권 대학 입시결과 발표", name = "dataName")
    private String dataName;

    @Schema(description = "dataLink", example = "https://", name = "dataLink")
    private String dataLink;

    @Schema(description = "viewCount", example = "2000", name = "viewCount")
    private Long viewCount;
}
