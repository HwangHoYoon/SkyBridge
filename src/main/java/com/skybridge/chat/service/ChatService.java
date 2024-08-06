package com.skybridge.chat.service;

import com.skybridge.chat.dto.ChatCard;
import com.skybridge.chat.dto.ChatLink;
import com.skybridge.chat.dto.ChatRes;
import com.skybridge.log.dto.ApiLogReq;
import com.skybridge.log.service.ApiLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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

    @Value("${domain}")
    private String domain;

    private final ApiLogService apiLogService;
    public ResponseEntity<Flux<String>> chat(String content) {
/*        String encodedString = Base64.getEncoder().encodeToString(content.getBytes());
        List<String> collectedData = new ArrayList<>();
        String url = base + chat + "/" + encodedString;
        log.info("chat api url : {}", url);
        Flux<String> stringFlux = webClient.get()
                .uri(url) // 스트리밍 엔드포인트
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(collectedData::add)
                .doOnComplete(() -> {
                    log.info("chat api Received all data: {}", collectedData);
                    ApiLogReq apiLogReq = new ApiLogReq();
                    apiLogReq.setUrl(url);
                    apiLogReq.setReqDate(LocalDate.now());
                    apiLogReq.setRes(collectedData.toString());
                    apiLogService.saveLog(apiLogReq);
                }).map(data -> {
                    data = data.replace("data:", "");
                    if (data.contains("source:")) {
                        data = "";
                    }
                    return data;
                }).filter(data -> !isWhitespaceOnly(data));*/
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(getStream(content));
    }

    public ResponseEntity<ChatRes> test(String content) {
        ChatRes chatRes = new ChatRes();
        if (StringUtils.equals(content, "1")) {
            chatRes.setContent("카드 테스트 입니다.");
            ChatCard chatCard = new ChatCard();
            chatCard.setImage(domain + "/file/file/ebs");
            chatCard.setTitle("[2024 수능특강] 최서희의 문학");
            chatCard.setSubject("국어영역 | 문학");
            chatCard.setTeacher("최서희");
            chatCard.setLevel("초급");
            chatCard.setTarget("고3");
            chatRes.setCard(chatCard);
        } else if (StringUtils.equals(content, "2")) {
            List<ChatLink> chatLinks = new ArrayList<>();
            chatRes.setContent("링크 테스트 입니다.");
            ChatLink chatLink = new ChatLink();
            chatLink.setImage(domain + "/file/file/ebs2");
            chatLink.setTitle("ebsi.co.kr");
            chatLink.setType("L");
            chatLink.setUrl("https://www.ebsi.co.kr/ebs/pot/poti/main.ebs");
            chatLinks.add(chatLink);

            ChatLink chatLink2 = new ChatLink();
            chatLink2.setImage(domain + "/file/file/mega");
            chatLink2.setTitle("megastudy.net");
            chatLink2.setType("L");
            chatLink2.setUrl("https://www.megastudy.net/");
            chatLinks.add(chatLink2);

            ChatLink chatLink3 = new ChatLink();
            chatLink3.setImage(domain + "/file/file/pdf");
            chatLink3.setTitle("고려대학교 2025 입시요강");
            chatLink3.setType("L");
            chatLink3.setUrl("https://www.megastudy.net/");
            chatLinks.add(chatLink3);

            chatRes.setLinkList(chatLinks);
        } else {
            String encodedString = Base64.getEncoder().encodeToString(content.getBytes());
            List<String> collectedData = new ArrayList<>();
            String url = base + chat + "/" + encodedString;
            log.info("chat api url : {}", url);
            String contentData = webClient.get()
                    .uri(url) // 스트리밍 엔드포인트
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            chatRes.setContent(contentData);
        }

/*        String encodedString = Base64.getEncoder().encodeToString(content.getBytes());
        List<String> collectedData = new ArrayList<>();
        String url = base + chat + "/" + encodedString;
        log.info("chat api url : {}", url);
        Flux<String> stringFlux = webClient.get()
                .uri(url) // 스트리밍 엔드포인트
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(collectedData::add)
                .doOnComplete(() -> {
                    log.info("chat api Received all data: {}", collectedData);
                    ApiLogReq apiLogReq = new ApiLogReq();
                    apiLogReq.setUrl(url);
                    apiLogReq.setReqDate(LocalDate.now());
                    apiLogReq.setRes(collectedData.toString());
                    apiLogService.saveLog(apiLogReq);
                }).map(data -> {
                    data = data.replace("data:", "");
                    if (data.contains("source:")) {
                        data = "";
                    }
                    return data;
                }).filter(data -> !isWhitespaceOnly(data));*/

        return ResponseEntity.ok().body(chatRes);
    }

    private boolean isWhitespaceOnly(String data) {
        return data.trim().isEmpty();
    }

    private Flux<String> getStream(String content) {
        String encodedString = Base64.getEncoder().encodeToString(content.getBytes());
        List<String> collectedData = new ArrayList<>();
        String url = base + chat + "/" + encodedString;
        log.info("chat api url : {}", url);
        return webClient.get()
                .uri(url) // 스트리밍 엔드포인트
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(collectedData::add)
                .doOnComplete(() -> {
                    log.info("chat api Received all data: {}", collectedData);
                    ApiLogReq apiLogReq = new ApiLogReq();
                    apiLogReq.setUrl(url);
                    apiLogReq.setReq(content);
                    apiLogReq.setReqDate(LocalDate.now());
                    apiLogReq.setRes(collectedData.toString());
                    apiLogService.saveLog(apiLogReq);
                }).map(data -> {
                    data = data.replace("data:", "");
                    if (data.contains("source:")) {
                        data = "";
                    }
                    return data;
                }).filter(data -> !isWhitespaceOnly(data));
    }
}
