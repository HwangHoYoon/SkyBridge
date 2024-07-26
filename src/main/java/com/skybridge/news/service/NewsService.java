package com.skybridge.news.service;

import com.skybridge.common.exception.CommonErrorCode;
import com.skybridge.common.exception.CommonException;
import com.skybridge.news.dto.NewsRes;
import com.skybridge.news.entity.AdmNew;
import com.skybridge.news.repository.AdmNewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class NewsService {
    private final AdmNewRepository admNewRepository;

    @Value("${domain}")
    private String domain;

    public ResponseEntity<List<NewsRes>> news(String sort) {
        List<NewsRes> newsResList = new ArrayList<>();
        Sort sortColumn;
        if (StringUtils.equals("1", sort)) {
            sortColumn = Sort.by("regDate").descending();
        } else {
            sortColumn = Sort.by("viewCount").descending();
        }
        List<AdmNew> admNewList = admNewRepository.findAll(sortColumn);

        if (!admNewList.isEmpty()) {
            admNewList.forEach(admNew -> {
                LocalDate currentDate = admNew.getRegDate();
                String regDateText = currentDate.getYear() + "." + currentDate.getMonthValue() + "." + currentDate.getDayOfMonth();
                NewsRes newsRes = new NewsRes();
                newsRes.setId(admNew.getId());
                newsRes.setNewsName(admNew.getNewsName());
                newsRes.setNewsLink(admNew.getNewsLink());
                newsRes.setNewsType(admNew.getNewsType());
                /*try {
                    newsRes.setNewsImage(encodeImageToBase64(admNew.getNewsImage()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/

                newsRes.setNewsImage(domain + "news/" +"image/" + admNew.getId());

                newsRes.setRegDate(currentDate);
                newsRes.setRegDateText(regDateText);
                newsRes.setViewCount(admNew.getViewCount());
                newsResList.add(newsRes);
            });
        }

        return ResponseEntity.ok(newsResList);
    }

    public String encodeImageToBase64(String imagePath) throws IOException {
        //Resource resource = new ClassPathResource(imagePath);
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public ResponseEntity<Resource> loadImage(String id) {
        if (StringUtils.isBlank(id)) {
            log.error("선생님 이미지 조회 정보가 잘못되었습니다. {}", id);
            throw new CommonException(CommonErrorCode.INVALID_ID);
        }

        Long resultId = Long.parseLong(id);
        Optional<AdmNew> admNew = admNewRepository.findById(resultId);

        if (admNew.isPresent()) {
            String newsImage = admNew.get().getNewsImage();
            Resource resource = new FileSystemResource(newsImage);
            if (!resource.exists())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            HttpHeaders header = new HttpHeaders();
            Path filePath = null;
            try {
                filePath = Paths.get(newsImage);
                header.add("Content-type", Files.probeContentType(filePath));
            } catch(IOException e) {
                log.error("이미지를 불러오는대 실패하였습니다. {}", newsImage);
            }
            return new ResponseEntity<>(resource, header, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
