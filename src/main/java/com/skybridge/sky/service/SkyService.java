package com.skybridge.sky.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skybridge.common.exception.CommonErrorCode;
import com.skybridge.common.exception.CommonException;
import com.skybridge.log.dto.ApiLogReq;
import com.skybridge.log.service.ApiLogService;
import com.skybridge.sky.dto.*;
import com.skybridge.sky.entity.SkyResult;
import com.skybridge.sky.entity.Teacher;
import com.skybridge.sky.repository.SkyResultRepository;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final ApiLogService apiLogService;

    private final SkyResultService skyResultService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<SkyRes> sykAi(String year, String subject, String grade, String university) throws IOException {
        //String encodedSubject = Base64.getEncoder().encodeToString(subject.getBytes());
        //String encodedUniversity = Base64.getEncoder().encodeToString(university.getBytes());
        //String url = base + gooroom + "?year=" + year + "&subject=" + encodedSubject + "&grade=" + grade + "&university=" + encodedUniversity;

        String url = base + gooroom + "?year=" + year + "&subject=" + subject + "&grade=" + grade + "&university=" + university;

        log.info("sykAi api url : {}", url);

        String responseData = webClient.get()
                .uri(url) // 스트리밍 엔드포인트
                .retrieve()
                .bodyToMono(String.class)
                .block();

        LocalDate localDate = LocalDate.now();
        ApiLogReq apiLogReq = new ApiLogReq();
        apiLogReq.setUrl(url);
        apiLogReq.setReqDate(localDate);
        apiLogReq.setRes(responseData);
        apiLogService.saveLog(apiLogReq);

        //String responseData = "{\"intro\": {\"contents\" : [\"영어 1등급이라니 정말 대단한데! 서울대학교에 합격하기 위해서는 조금 더 노력해야 할 수도 있어. 내가 도와줄게!\"]},\"1\": {\"date\": [2024.8, 2025.4], \"title\": \"고급 영단어 암기\", \"contents\" : [\"서울대학교 수준에 맞는 고급 영단어를 암기합니다.\"]},\"2\": {\"date\": [2025.5, 2025.10], \"title\": \"심화 영문법 학습 및 문제 풀이\", \"contents\" : [\"더욱 심화된 영문법을 학습하며 다양한 문제를 풀어봅니다.\"]},\"3\": {\"date\": [2025.11, 2026.5], \"title\": \"영어 논문 읽기와 에세이 작성\", \"contents\" : [\"수준 높은 영어 논문을 읽으며 독해력을 기릅니다.\",\"이를 바탕으로 영어 에세이를 작성하며 작문 실력을 키웁니다.\"]},\"4\": {\"date\": [2026.6, 2026.11], \"title\": \"모의고사 연습 및 실전 대비\", \"contents\" : [\"실제 수능과 유사한 모의고사를 풀면서 시간 분배와 문제 해결 능력을 익힙니다.\"]},\"5\": {\"date\": [2026.12, 2027.5], \"title\": \"영어 토론 및 발표 연습\", \"contents\" : [\"영어로 토론하고 발표하는 연습을 하면서 회화 실력과 자신감을 높입니다.\"]},\"6\": {\"date\": [2027.6, 2028.11], \"title\": \"최종 마무리\", \"contents\" : [\"지금까지 학습한 내용을 복습하면서 부족한 부분을 보완합니다.\",\"최근 3개년 수능 기출문제를 다시 한 번 분석하여 출제 경향을 파악합니다.\"]},\"outro\": {\"contents\" : [\"이렇게 체계적으로 공부하면 서울대학교에 합격할 수 있을 거야. 물론 쉽지 않겠지만, 너의 노력과 열정이라면 충분히 해낼 수 있어! 응원할게\"]}}";
        StudyPlanWrapper studyPlan = jsonPassing(responseData, StudyPlanWrapper.class);
        log.info("sykAi api Received all data: {}", studyPlan);
        log.info("sykAi api Received all data: {}", responseData);

        SkyResult skyResult = new SkyResult();
        skyResult.setYear(year);
        skyResult.setSubject(subject);
        skyResult.setGrade(grade);
        skyResult.setUniversity(university);
        skyResult.setResponse(responseData);
        skyResult.setRegDate(localDate);
        skyResultService.saveResult(skyResult);

        //String base64Image = teacherImage(subject);
        //  img.src = 'data:image/jpeg;base64,' + base64Image;

        List<SkyResultShortRes> skyResultShortResList = new ArrayList<>();
        if (studyPlan != null) {
            studyPlan.getPlans().forEach((key, plan) -> {
                List<String> dates = plan.getDate();
                SkyResultShortRes skyResultShortRes = new SkyResultShortRes();
                if (dates.size() == 2) {
                    skyResultShortRes.setStartDate(dates.get(0));
                    skyResultShortRes.setEndDate(dates.get(1));
                }
                skyResultShortRes.setTitle(plan.getTitle());
                skyResultShortResList.add(skyResultShortRes);
            });
        }

        Teacher teacher = teacherRepository.findRandomTeacherBySubject(subject);
        SkyRes skyRes = new SkyRes();
        skyRes.setTeacherImage(domain + "sky/" + "image/" + teacher.getId());
        skyRes.setPlanList(skyResultShortResList);
        skyRes.setResultId(skyResult.getId());

        return ResponseEntity.ok().body(skyRes);
    }

    public ResponseEntity<String> reloadTeacher(Long resultId) {
        //String teacherImage = teacherImage(subject);
        SkyResult result = skyResultService.getResult(resultId);
        String subject = result.getSubject();
        Teacher teacher = teacherRepository.findRandomTeacherBySubject(subject);
        String teacherImage = domain + "sky/" +"image/" + teacher.getId();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE).body(teacherImage);
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

    public ResponseEntity<SkyResultRes> result(Long resultId) {
        SkyResult result = skyResultService.getResult(resultId);

        String responseData = result.getResponse();
        StudyPlanWrapper studyPlan = jsonPassing(responseData, StudyPlanWrapper.class);

        SkyResultRes resultRes = new SkyResultRes();
        resultRes.setResultId(resultId);
        if (studyPlan != null) {
            List<String> introContents = studyPlan.getIntro().getContents();
            List<String> outroContents = studyPlan.getOutro().getContents();;
            String introText = introContents.stream().collect(Collectors.joining(System.lineSeparator()));
            String outroText = outroContents.stream().collect(Collectors.joining(System.lineSeparator()));

            resultRes.setIntro(introText);
            resultRes.setOutro(outroText);

            List<SkyResultLongRes> planList = new ArrayList<>();
            studyPlan.getPlans().forEach((key, plan) -> {
                SkyResultLongRes skyResultLongRes = new SkyResultLongRes();

                List<String> dates = plan.getDate();
                if (dates.size() == 2) {
                    skyResultLongRes.setStartDate(dates.get(0));
                    skyResultLongRes.setEndDate(dates.get(1));
                }
                skyResultLongRes.setTitle(plan.getTitle());

                List<String> contents = plan.getContents();
                String contentText = contents.stream().collect(Collectors.joining(System.lineSeparator()));
                skyResultLongRes.setContent(contentText);
                planList.add(skyResultLongRes);
            });
            resultRes.setPlanList(planList);
        }
        return ResponseEntity.ok(resultRes);
    }

    private <T> T jsonPassing(String jsonData, Class<T> tClass) {
        try {
            return objectMapper.readValue(jsonData, tClass);
        } catch (Exception e) {
            log.error("Json passing error {}" ,  e.getMessage());
            try {
                return tClass.getDeclaredConstructor().newInstance();
            } catch (Exception ei) {
                log.error("Json passing error error {}" ,  e.getMessage());
                return null;
            }
        }
    }
}
