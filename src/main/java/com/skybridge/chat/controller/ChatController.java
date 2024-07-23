package com.skybridge.chat.controller;

import com.skybridge.chat.service.ChatService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "chat", description = "채팅 API")
@RequestMapping("chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping(value = "/quest/{content}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@Schema(description = "content", example = "고려대학교 입시요강 알려줘", name = "content") @PathVariable("content")  String content) {
        return chatService.chat(content);
    }

}