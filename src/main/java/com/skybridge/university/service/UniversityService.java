package com.skybridge.university.service;

import com.skybridge.university.entity.University;
import com.skybridge.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UniversityService {
    private final UniversityRepository universityRepository;

    public ResponseEntity<List<String>> university(String name) {
        List<String> universities = new ArrayList<>();
        List<University> universityList;
        if (name == null || name.isEmpty()) {
            Sort sortColumn = Sort.by("name").ascending();
            universityList = universityRepository.findAll(sortColumn);
        } else {
            universityList = universityRepository.findByNameContainsOrderByNameAsc(name);
        }
        if (!universityList.isEmpty()) {
            universityList.forEach(university -> universities.add(university.getName()));
        }
        return ResponseEntity.ok(universities);
    }
}
