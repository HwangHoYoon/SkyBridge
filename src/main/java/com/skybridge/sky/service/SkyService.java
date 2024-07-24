package com.skybridge.sky.service;

import com.skybridge.common.exception.CommonErrorCode;
import com.skybridge.common.exception.CommonException;
import com.skybridge.sky.dto.SkyRes;
import com.skybridge.sky.entity.Teacher;
import com.skybridge.sky.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkyService {
    private final WebClient webClient;
    private final TeacherRepository teacherRepository;

    @Value("${api.url.base}")
    private String base;

    @Value("${api.url.gooroom}")
    private String gooroom;

    @Value("${domain}")
    private String domain;

    public ResponseEntity<SkyRes> sykAi(String year, String subject, String grade, String university) throws IOException {
        String encodedSubject = Base64.getEncoder().encodeToString(subject.getBytes());
        String encodedUniversity = Base64.getEncoder().encodeToString(university.getBytes());

        String responseData = webClient.get()
                .uri(base + gooroom + "?year=" + year + "&subject=" + encodedSubject + "&grade=" + grade + "&university=" + encodedUniversity) // 스트리밍 엔드포인트
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //String base64Image = teacherImage(subject);

        //  img.src = 'data:image/jpeg;base64,' + base64Image;
        Teacher teacher = teacherRepository.findRandomTeacherBySubject(subject);
        SkyRes skyRes = new SkyRes();
        skyRes.setTeacherImage(domain + "image/" + teacher.getId());
        skyRes.setPlanText(responseData);
        skyRes.setSubject(subject);

        return ResponseEntity.ok().body(skyRes);
    }

    public ResponseEntity<String> reloadTeacher(String subject) throws IOException {
        //String teacherImage = teacherImage(subject);
        Teacher teacher = teacherRepository.findRandomTeacherBySubject(subject);
        String teacherImage = domain + "sky/" +"image/" + teacher.getId();
        return ResponseEntity.ok().body(teacherImage);
    }

    private String teacherImage(String subject) throws IOException {
        Teacher teacher = teacherRepository.findRandomTeacherBySubject(subject);
        //Resource resource = new ClassPathResource(teacher.getTeacherImage());
        byte[] imageBytes = Files.readAllBytes(Paths.get(teacher.getTeacherImage()));
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public ResponseEntity<Resource> loadImage(String id) {
        if (StringUtils.isBlank(id)) {
            log.error("선생님 이미지 조회 정보가 잘못되었습니다. {}", id);
            throw new CommonException(CommonErrorCode.INVALID_ID);
        }

        Long resultId = Long.parseLong(id);
        Optional<Teacher> teacher = teacherRepository.findById(resultId);

        if (teacher.isPresent()) {
            String teacherImage = teacher.get().getTeacherImage();
            Resource resource = new FileSystemResource(teacherImage);
            if (!resource.exists())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            HttpHeaders header = new HttpHeaders();
            Path filePath = null;
            try {
                filePath = Paths.get(teacherImage);
                header.add("Content-type", Files.probeContentType(filePath));
            } catch(IOException e) {
                log.error("이미지를 불러오는대 실패하였습니다. {}", teacherImage);
            }
            return new ResponseEntity<>(resource, header, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
