package com.skybridge.tmp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"Tmp\"")
public class Tmp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fld01", length = Integer.MAX_VALUE)
    private String fld01;

    @Column(name = "fld02", length = Integer.MAX_VALUE)
    private String fld02;

    @Column(name = "fld03", length = Integer.MAX_VALUE)
    private String fld03;

}