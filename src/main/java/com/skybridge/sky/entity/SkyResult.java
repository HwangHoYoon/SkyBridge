package com.skybridge.sky.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "sky_result")
public class SkyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year", length = Integer.MAX_VALUE)
    private String year;

    @Column(name = "subject", length = Integer.MAX_VALUE)
    private String subject;

    @Column(name = "grade", length = Integer.MAX_VALUE)
    private String grade;

    @Column(name = "university", length = Integer.MAX_VALUE)
    private String university;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

    @Column(name = "response", length = Integer.MAX_VALUE)
    private String response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}