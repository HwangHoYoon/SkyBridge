package com.skybridge.chat.controller;

import com.skybridge.chat.dto.ChatRes;
import com.skybridge.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "질문", description = "질문")
    public ResponseEntity<Flux<String>> chat(@Schema(description = "content", example = "고려대학교 입시요강 알려줘", name = "content") @PathVariable("content")  String content) {
        return chatService.chat(content);
    }

    @GetMapping(value = "/test/{content}")
    @Operation(summary = "질문", description = "질문")
    public ResponseEntity<ChatRes> test(@Schema(description = "content", example = "고려대학교 입시요강 알려줘", name = "content") @PathVariable("content")  String content) {
        return chatService.test(content);
    }

}
