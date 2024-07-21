package com.skybridge.review.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @NotNull
    @Column(name = "subject", nullable = false, length = Integer.MAX_VALUE)
    private String subject;

    @NotNull
    @Column(name = "score", nullable = false, length = Integer.MAX_VALUE)
    private String score;

    @NotNull
    @Column(name = "bad", nullable = false, length = Integer.MAX_VALUE)
    private String bad;

    @NotNull
    @Column(name = "good", nullable = false, length = Integer.MAX_VALUE)
    private String good;

    @NotNull
    @Column(name = "tag", nullable = false, length = Integer.MAX_VALUE)
    private String tag;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

}