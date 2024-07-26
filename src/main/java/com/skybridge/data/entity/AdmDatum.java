package com.skybridge.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "adm_data")
public class AdmDatum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "data_name", nullable = false, length = Integer.MAX_VALUE)
    private String dataName;

    @NotNull
    @Column(name = "data_link", nullable = false, length = Integer.MAX_VALUE)
    private String dataLink;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

}