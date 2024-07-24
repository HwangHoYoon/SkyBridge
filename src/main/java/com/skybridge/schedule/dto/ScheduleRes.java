package com.skybridge.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleRes {

    @Schema(description = "scheduleName", example = "원서접수", name = "scheduleName")
    private String scheduleName;

    @Schema(description = "scheduleDate", example = "2024-08-12", name = "scheduleDate")
    private LocalDate scheduleDate;

    @Schema(description = "scheduleDateText", example = "8월 12일", name = "scheduleDateText")
    private String scheduleDateText;

    @Schema(description = "scheduleType", example = "수시", name = "scheduleType")
    private String scheduleType;

    @Schema(description = "dDay", example = "D-19", name = "dDay")
    private String dDay;
}
