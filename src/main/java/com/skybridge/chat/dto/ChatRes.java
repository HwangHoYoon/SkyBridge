package com.skybridge.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ChatRes {

    @Schema(description = "content", example = "고려대학교 입시요강은...", name = "content")
    private String content;

    @Schema(description = "card", name = "card")
    private ChatCard card;

    @Schema(description = "linkList", name = "linkList")
    private List<ChatLink> linkList;
}
