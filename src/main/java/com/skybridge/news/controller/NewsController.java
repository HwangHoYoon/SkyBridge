package com.skybridge.news.controller;

import com.skybridge.data.dto.DataRes;
import com.skybridge.news.dto.NewsRes;
import com.skybridge.news.service.NewsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "news", description = "뉴스 API")
@RequestMapping("news")
public class NewsController {
    private final NewsService newsService;

    @GetMapping(value = "/news")
    public ResponseEntity<List<NewsRes>> news() {
        return newsService.news();
    }

}
