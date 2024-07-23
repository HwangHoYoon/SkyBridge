package com.skybridge.log.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "api_log")
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false, length = Integer.MAX_VALUE)
    private String url;

    @Column(name = "req", length = Integer.MAX_VALUE)
    private String req;

    @Column(name = "res", length = Integer.MAX_VALUE)
    private String res;

    @NotNull
    @Column(name = "req_date", nullable = false)
    private LocalDate reqDate;

}