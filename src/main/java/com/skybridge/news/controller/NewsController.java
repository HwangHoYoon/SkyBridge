package com.skybridge.news.controller;

import com.skybridge.news.dto.NewsRes;
import com.skybridge.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "news", description = "뉴스 API")
@RequestMapping("news")
public class NewsController {
    private final NewsService newsService;

    @GetMapping(value = "/news")
    @Operation(summary = "뉴스", description = "뉴스")
    public ResponseEntity<List<NewsRes>> news() {
        return newsService.news();
    }

    @GetMapping("/image/{id}")
    @Operation(summary = "뉴스 이미지 조회", description = "이미지 조회")
    public ResponseEntity<Resource>  loadImage(@PathVariable("id") String id) {
        return newsService.loadImage(id);
    }

}
