package com.skybridge.sky.service;

import com.skybridge.sky.dto.SkyRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkyService {
    private final WebClient webClient;

    public SkyRes gooroom(String year, String subject, String grade, String university) {
        String encodedSubject = Base64.getEncoder().encodeToString(subject.getBytes());
        String encodedUniversity = Base64.getEncoder().encodeToString(university.getBytes());

        String responseData = webClient.get()
                .uri("http://223.130.129.246:8000/gooroom?year=" + year + "&subject=" + encodedSubject + "&grade=" + grade + "&university=" + encodedUniversity) // 스트리밍 엔드포인트
                .retrieve()
                .bodyToMono(String.class)
                .block();

        SkyRes skyRes = new SkyRes();
        skyRes.setTeacherUrl(responseData);
        skyRes.setPlanText("https://text");

        return skyRes;
    }
}
