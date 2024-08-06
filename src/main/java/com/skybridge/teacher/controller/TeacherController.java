package com.skybridge.teacher.controller;

import com.skybridge.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

/*    @Scheduled(cron = "0 20 22 * * ?")
    public void runTask() throws Exception {
        log.info("teacherService Scheduled start");
        teacherService.teacher();
        log.info("teacherService Scheduled end");
    }*/
}
