package com.skybridge.news.service;

import com.skybridge.news.dto.NewsRes;
import com.skybridge.news.entity.AdmNew;
import com.skybridge.news.repository.AdmNewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class NewsService {
    private final AdmNewRepository admNewRepository;

    public ResponseEntity<List<NewsRes>> news() {
        List<NewsRes> newsResList = new ArrayList<>();

        Sort sort = Sort.by("viewCount").ascending();
        List<AdmNew> admNewList = admNewRepository.findAll(sort);

        if (!admNewList.isEmpty()) {
            admNewList.forEach(admNew -> {
                LocalDate currentDate = admNew.getRegDate();
                String regDateText = currentDate.getYear() + "." + currentDate.getMonthValue() + "." + currentDate.getDayOfMonth();
                NewsRes newsRes = new NewsRes();
                newsRes.setNewsName(admNew.getNewsName());
                newsRes.setNewsLink(admNew.getNewsLink());
                try {
                    newsRes.setNewsImage(encodeImageToBase64(admNew.getNewsImage()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
}
