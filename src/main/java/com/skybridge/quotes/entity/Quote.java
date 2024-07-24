package com.skybridge.quotes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "quotes")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "quote", nullable = false, length = Integer.MAX_VALUE)
    private String quote;

    @NotNull
    @Column(name = "author", nullable = false, length = Integer.MAX_VALUE)
    private String author;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

}