package com.skybridge.file.repository;

import com.skybridge.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    File findByKey(String key);
}