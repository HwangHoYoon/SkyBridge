package com.skybridge.university.repository;

import com.skybridge.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Long> {

    List<University> findByNameContainsOrderByNameAsc(String name);
}