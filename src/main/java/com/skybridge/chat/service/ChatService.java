package com.skybridge.chat.service;

import com.skybridge.log.dto.ApiLogReq;
import com.skybridge.log.service.ApiLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {
    private final WebClient webClient;

    @Value("${api.url.base}")
    private String base;

    @Value("${api.url.chat}")
    private String chat;

    private final ApiLogService apiLogService;

    public Flux<String> chat(String content) {
        String encodedString = Base64.getEncoder().encodeToString(content.getBytes());
        List<String> collectedData = new ArrayList<>();
        String url = base + chat + "/" + encodedString;
        return webClient.get()
                .uri(url) // 스트리밍 엔드포인트
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(collectedData::add)
                .doOnComplete(() -> {
                    log.info("Received all data: {}", collectedData);
                    ApiLogReq apiLogReq = new ApiLogReq();
                    apiLogReq.setUrl(url);
                    apiLogReq.setReqDate(LocalDate.now());
                    apiLogReq.setRes(collectedData.toString());
                    apiLogService.saveLog(apiLogReq);
                });
    }

}
