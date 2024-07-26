package com.skybridge.sky.controller;

import com.skybridge.chat.service.ChatService;
import com.skybridge.sky.dto.SkyRes;
import com.skybridge.sky.dto.SkyResultRes;
import com.skybridge.sky.service.SkyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "sky", description = "스카이 AI API")
@RequestMapping("sky")
public class SkyController {
    private final SkyService skyService;

    @GetMapping(value = "/ai/{year}/{subject}/{grade}/{university}")
    @Operation(summary = "SKY AI", description = "SKY AI")
    public ResponseEntity<SkyRes> gooroom(@Schema(description = "year", example = "2025", name = "year") @PathVariable("year") String year,
                                          @Schema(description = "subject", example = "수학", name = "subject") @PathVariable("subject") String subject,
                                          @Schema(description = "grade", example = "2", name = "grade") @PathVariable("grade") String grade,
                                          @Schema(description = "university", example = "고려대학교", name = "university") @PathVariable("university") String university
    ) throws IOException {
        return skyService.sykAi(year, subject, grade, university);
    }

    @GetMapping(value = "/reloadTeacher/{resultId}")
    @Operation(summary = "선생님 새로고침", description = "선생님 새로고침")
    public ResponseEntity<String> reloadTeacher(
        @Schema(description = "resultId", example = "1", name = "resultId") @PathVariable("resultId") Long resultId
    ) {
        return skyService.reloadTeacher(resultId);
    }

    @GetMapping("/image/{id}")
    @Operation(summary = "선생님 이미지 조회", description = "선생님 이미지 조회")
    public ResponseEntity<Resource>  loadImage(@PathVariable("id") String id) {
        return skyService.loadImage(id);
    }

    @GetMapping(value = "/result/{resultId}")
    @Operation(summary = "계획 결과 조회", description = "계획 결과 조회")
    public ResponseEntity<SkyResultRes> result(@Schema(description = "resultId", example = "1", name = "resultId") @PathVariable("resultId") Long resultId
    ) {
        return skyService.result(resultId);
    }
}
