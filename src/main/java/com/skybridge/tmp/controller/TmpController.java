package com.skybridge.tmp.controller;

import com.microsoft.playwright.*;
import com.skybridge.tmp.entity.Tmp;
import com.skybridge.tmp.service.TmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TmpController {
    private final TmpService tmpService;

    @GetMapping("/")
    public List<Tmp> getTmp() {
        return tmpService.selectTmp();
    }
}
