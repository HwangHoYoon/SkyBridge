package com.skybridge.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChatLink {

    @Schema(description = "image", example = "https://", name = "image")
    private String image;

    @Schema(description = "title", example = "입시요강 및 a.com", name = "title")
    private String title;

    @Schema(description = "type", example = "Link(L) or File(F)", name = "type")
    private String type;

    @Schema(description = "url", example = "https://", name = "url")
    private String url;
}
