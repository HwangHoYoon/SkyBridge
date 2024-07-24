package com.skybridge.quotes.controller;

import com.skybridge.quotes.dto.QuotesRes;
import com.skybridge.quotes.service.QuotesService;
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
@Tag(name = "quotes", description = "명언 API")
@RequestMapping("quotes")
public class QuotesController {

    private final QuotesService quotesService;

    @GetMapping(value = "/quotes")
    public ResponseEntity<List<QuotesRes>> quotes() {
        return quotesService.quotes();
    }
}
