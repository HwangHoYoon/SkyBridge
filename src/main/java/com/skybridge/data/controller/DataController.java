package com.skybridge.data.controller;

import com.skybridge.data.dto.DataRes;
import com.skybridge.data.service.DataService;
import com.skybridge.quotes.dto.QuotesRes;
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
@Tag(name = "data", description = "입시자료 API")
@RequestMapping("data")
public class DataController {
    private final DataService dataService;

    @GetMapping(value = "/data")
    public ResponseEntity<List<DataRes>> data() {
        return dataService.data();
    }
}
