package com.skybridge.sky.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "teacher")
public class Teacher {

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
    @Column(name = "file_path", nullable = false, length = Integer.MAX_VALUE)
    private String filePath;

    @Column(name = "file_name", nullable = false, length = Integer.MAX_VALUE)
    private String fileName;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

}
