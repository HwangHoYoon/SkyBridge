package com.skybridge.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Data
public class NewsRes {
    @Schema(description = "id", example = "id", name = "id")
    private Long id;

    @Schema(description = "newsName", example = "뉴스 내용", name = "newsName")
    private String newsName;

    @Schema(description = "newsLink", example = "https://", name = "newsLink")
    private String newsLink;

    @Schema(description = "newsImage", example = "abcd", name = "newsImage")
    private String newsImage;

    @Schema(description = "viewCount", example = "3000", name = "viewCount")
    private Long viewCount;

    @Schema(description = "regDate", example = "2024-07-24", name = "regDate")
    private LocalDate regDate;

    @Schema(description = "regDateText", example = "2024.03.01", name = "regDateText")
    private String regDateText;

}
