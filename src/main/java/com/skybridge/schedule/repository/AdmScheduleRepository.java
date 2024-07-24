package com.skybridge.schedule.repository;

import com.skybridge.schedule.entity.AdmSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AdmScheduleRepository extends JpaRepository<AdmSchedule, Long> {

    List<AdmSchedule> findByScheduleDateAfterOrderByScheduleDateAsc(LocalDate scheduleDate);
}