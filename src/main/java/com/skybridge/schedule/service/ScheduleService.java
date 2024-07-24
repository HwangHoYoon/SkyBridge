package com.skybridge.schedule.service;

import com.skybridge.schedule.dto.ScheduleRes;
import com.skybridge.schedule.entity.AdmSchedule;
import com.skybridge.schedule.repository.AdmScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScheduleService {
    private final AdmScheduleRepository admScheduleRepository;

    public ResponseEntity<List<ScheduleRes>> schedule() {
        List<ScheduleRes> scheduleResList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        List<AdmSchedule> admScheduleList = admScheduleRepository.findByScheduleDateAfterOrderByScheduleDateAsc(currentDate);
        if (!admScheduleList.isEmpty()) {
            admScheduleList.forEach(admSchedule -> {
                LocalDate scheduleDate = admSchedule.getScheduleDate();
                String month =  scheduleDate.getMonth().getValue() + "월 ";
                String day = scheduleDate.getDayOfMonth() + "일";

                long daysBetween = ChronoUnit.DAYS.between(currentDate, scheduleDate);

                ScheduleRes scheduleRes = new ScheduleRes();
                scheduleRes.setScheduleName(admSchedule.getScheduleName());
                scheduleRes.setScheduleDate(scheduleDate);
                scheduleRes.setScheduleDateText(month + day);
                scheduleRes.setScheduleType(admSchedule.getScheduleType());
                scheduleRes.setDDay("D-" + daysBetween);
                scheduleResList.add(scheduleRes);
            });
        }
        return ResponseEntity.ok(scheduleResList);
    }
}
