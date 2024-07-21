package com.skybridge.common.response;

import io.swagger.v3.oas.annotations.media.Schema;


public record CommonResponse<T>(@Schema(description = "결과코드", example = "200", name = "code")String code, T data, @Schema(description = "메세지", example = "OK", name = "message")String message) {

}
