package com.skybridge.file.controller;

import com.skybridge.chat.dto.ChatRes;
import com.skybridge.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "file", description = "파일 API")
@RequestMapping("file")
public class FileController {
    private final FileService fileService;

    @GetMapping("/file/{key}")
    @Operation(summary = "파일 조회", description = "파일 조회")
    public ResponseEntity<Resource> loadImage(@PathVariable("key") String key) {
        return fileService.loadImage(key);
    }
}
