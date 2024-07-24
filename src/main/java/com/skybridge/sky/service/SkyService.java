package com.skybridge.sky.service;

import com.skybridge.sky.dto.SkyRes;
import com.skybridge.sky.entity.Teacher;
import com.skybridge.sky.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

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

    public ResponseEntity<SkyRes> sykAi(String year, String subject, String grade, String university) throws IOException {
        String encodedSubject = Base64.getEncoder().encodeToString(subject.getBytes());
        String encodedUniversity = Base64.getEncoder().encodeToString(university.getBytes());

        String responseData = webClient.get()
                .uri(base + gooroom + "?year=" + year + "&subject=" + encodedSubject + "&grade=" + grade + "&university=" + encodedUniversity) // 스트리밍 엔드포인트
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String base64Image = teacherImage(subject);

        //  img.src = 'data:image/jpeg;base64,' + base64Image;

        SkyRes skyRes = new SkyRes();
        skyRes.setTeacherImage(base64Image);
        skyRes.setPlanText(responseData);
        skyRes.setSubject(subject);

        return ResponseEntity.ok().body(skyRes);
    }

    public ResponseEntity<String> reloadTeacher(String subject) throws IOException {
        String teacherImage = teacherImage(subject);
        return ResponseEntity.ok().body(teacherImage);
    }

    private String teacherImage(String subject) throws IOException {
        Teacher teacher = teacherRepository.findRandomTeacherBySubject(subject);
        Resource resource = new ClassPathResource(teacher.getTeacherImage());
        byte[] imageBytes = Files.readAllBytes(Paths.get(resource.getURI()));
        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
