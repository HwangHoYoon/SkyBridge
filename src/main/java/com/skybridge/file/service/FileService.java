package com.skybridge.file.service;

import com.skybridge.common.exception.CommonErrorCode;
import com.skybridge.common.exception.CommonException;
import com.skybridge.file.entity.File;
import com.skybridge.file.repository.FileRepository;
import com.skybridge.news.entity.AdmNew;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileService {
    private final FileRepository fileRepository;
    public ResponseEntity<Resource> loadImage(String key) {
        File admNew = fileRepository.findByKey(key);
        String fileName = admNew.getPath() + admNew.getName();
        Resource resource = new FileSystemResource(fileName);
        if (!resource.exists())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(fileName);
            header.add("Content-type", Files.probeContentType(filePath));
        } catch(IOException e) {
            log.error("파일을 불러오는대 실패하였습니다. {}", fileName);
        }
        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }
}
