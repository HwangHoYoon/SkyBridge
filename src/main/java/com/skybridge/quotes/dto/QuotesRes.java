package com.skybridge.quotes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QuotesRes {
    @Schema(description = "quote", example = "명언", name = "quote")
    private String quote;

    @Schema(description = "author", example = "작성자", name = "author")
    private String author;

}
