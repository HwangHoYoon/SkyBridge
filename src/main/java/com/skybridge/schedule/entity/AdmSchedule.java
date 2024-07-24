package com.skybridge.schedule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "adm_schedule")
public class AdmSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "schedule_name", nullable = false, length = Integer.MAX_VALUE)
    private String scheduleName;

    @NotNull
    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @NotNull
    @Column(name = "schedule_type", nullable = false, length = Integer.MAX_VALUE)
    private String scheduleType;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

}