package com.skybridge.university.controller;

import com.skybridge.schedule.dto.ScheduleRes;
import com.skybridge.university.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "university", description = "대학교 API")
@RequestMapping("university")
public class UniversityController {
    private final UniversityService universityService;

    @GetMapping(value = "/university")
    @Operation(summary = "대학목록", description = "대학목록")
    public ResponseEntity<List<String>> university() {
        return universityService.university(null);
    }

    @GetMapping(value = "/university/{name}")
    @Operation(summary = "대학목록", description = "대학목록")
    public ResponseEntity<List<String>> university(@PathVariable(value = "name", required = false) String name) {
        return universityService.university(name);
    }

}
