package com.skybridge.teacher.repository;

import com.skybridge.teacher.entity.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherInfoRepository extends JpaRepository<TeacherInfo, Long> {
}