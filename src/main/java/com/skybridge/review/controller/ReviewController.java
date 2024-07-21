package com.skybridge.review.controller;

import com.microsoft.playwright.*;
import com.skybridge.review.service.ReviewService;
import com.skybridge.tmp.entity.Tmp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/review")
    public void review() {

        reviewService.review();


    }

}
