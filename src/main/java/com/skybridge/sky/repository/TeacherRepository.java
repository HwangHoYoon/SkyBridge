package com.skybridge.sky.repository;

import com.skybridge.sky.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query(value = "SELECT * FROM teacher WHERE subject = :subject ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Teacher findRandomTeacherBySubject(@Param("subject") String subject);
}
