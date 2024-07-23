package com.skybridge.sky.controller;

import com.skybridge.chat.service.ChatService;
import com.skybridge.sky.dto.SkyRes;
import com.skybridge.sky.service.SkyService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "sky", description = "스카이 AI API")
@RequestMapping("sky")
public class SkyController {
    private final SkyService skyService;

    @GetMapping(value = "/ai/{year}/{subject}/{grade}/{university}")
    public SkyRes gooroom(@Schema(description = "year", example = "2025", name = "year") @PathVariable("year") String year,
                          @Schema(description = "subject", example = "수학", name = "subject") @PathVariable("subject") String subject,
                          @Schema(description = "grade", example = "2", name = "grade") @PathVariable("grade") String grade,
                          @Schema(description = "university", example = "고려대학교", name = "university") @PathVariable("university") String university
    ) {
        return skyService.gooroom(year, subject, grade, university);
    }
}
