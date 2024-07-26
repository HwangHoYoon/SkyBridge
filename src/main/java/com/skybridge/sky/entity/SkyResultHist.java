package com.skybridge.sky.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "sky_result_hist")
public class SkyResultHist {
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

    @Column(name = "response", length = Integer.MAX_VALUE)
    private String response;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "result_id", nullable = false)
    private SkyResult result;

    @NotNull
    @Column(name = "result_reg_date", nullable = false)
    private LocalDate resultRegDate;

}