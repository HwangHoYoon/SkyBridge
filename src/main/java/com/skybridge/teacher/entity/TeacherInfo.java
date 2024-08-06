package com.skybridge.teacher.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "teacher_info")
public class TeacherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "course_name", length = Integer.MAX_VALUE)
    private String courseName;

    @Column(name = "teacher_name", length = Integer.MAX_VALUE)
    private String teacherName;

    @Column(name = "course_scope", length = Integer.MAX_VALUE)
    private String courseScope;

    @Column(name = "course_features", length = Integer.MAX_VALUE)
    private String courseFeatures;

    @Column(name = "target_audience", length = Integer.MAX_VALUE)
    private String targetAudience;

    @Column(name = "course_info", length = Integer.MAX_VALUE)
    private String courseInfo;

    @Column(name = "subject", length = Integer.MAX_VALUE)
    private String subject;

    @Column(name = "learning_stage", length = Integer.MAX_VALUE)
    private String learningStage;

    @Column(name = "level", length = Integer.MAX_VALUE)
    private String level;

    @Column(name = "grade", length = Integer.MAX_VALUE)
    private String grade;

    @Column(name = "lecture_count", length = Integer.MAX_VALUE)
    private String lectureCount;

    @Column(name = "textbook", length = Integer.MAX_VALUE)
    private String textbook;

    @Column(name = "course_interest_count", length = Integer.MAX_VALUE)
    private String courseInterestCount;

    @Column(name = "average_rating", length = Integer.MAX_VALUE)
    private String averageRating;

    @Column(name = "related_series", length = Integer.MAX_VALUE)
    private String relatedSeries;

    @Column(name = "learner_distribution", length = Integer.MAX_VALUE)
    private String learnerDistribution;

    @Column(name = "senior_year", length = Integer.MAX_VALUE)
    private String seniorYear;

    @Column(name = "junior_year", length = Integer.MAX_VALUE)
    private String juniorYear;

    @Column(name = "sophomore_year", length = Integer.MAX_VALUE)
    private String sophomoreYear;

    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;

    @NotNull
    @Column(name = "reg_date", nullable = false)
    private LocalDate regDate;

}