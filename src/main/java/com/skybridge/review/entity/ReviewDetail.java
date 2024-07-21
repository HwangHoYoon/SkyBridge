package com.skybridge.review.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "review_detail")
public class ReviewDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "job", length = Integer.MAX_VALUE)
    private String job;

    @Column(name = "major", length = Integer.MAX_VALUE)
    private String major;

    @Column(name = "region", length = Integer.MAX_VALUE)
    private String region;

    @Column(name = "score", length = Integer.MAX_VALUE)
    private String score;

    @Column(name = "bad", length = Integer.MAX_VALUE)
    private String bad;

    @Column(name = "good", length = Integer.MAX_VALUE)
    private String good;

    @Column(name = "tag", length = Integer.MAX_VALUE)
    private String tag;

    @Column(name = "reg_date")
    private LocalDate regDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

}