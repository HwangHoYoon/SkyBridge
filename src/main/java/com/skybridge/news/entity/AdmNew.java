package com.skybridge.news.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "adm_news")
public class AdmNew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "news_name", nullable = false, length = Integer.MAX_VALUE)
    private String newsName;

    @Column(name = "news_link", length = Integer.MAX_VALUE)
    private String newsLink;

    @Column(name = "news_image", length = Integer.MAX_VALUE)
    private String newsImage;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

}