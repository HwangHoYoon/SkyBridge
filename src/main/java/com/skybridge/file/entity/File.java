package com.skybridge.file.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "path", length = Integer.MAX_VALUE)
    private String path;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

    @Column(name = "key", length = Integer.MAX_VALUE)
    private String key;

}