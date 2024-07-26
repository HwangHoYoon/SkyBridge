package com.skybridge.schedule.controller;

import com.skybridge.quotes.dto.QuotesRes;
import com.skybridge.schedule.dto.ScheduleRes;
import com.skybridge.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "schedule", description = "일정 API")
@RequestMapping("schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping(value = "/schedule")
    @Operation(summary = "입시일정", description = "입시일정")
    public ResponseEntity<List<ScheduleRes>> schedule() {
        return scheduleService.schedule();
    }
}
