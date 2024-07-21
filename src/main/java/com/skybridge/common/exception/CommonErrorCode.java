package com.skybridge.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(String.valueOf(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(String.valueOf(HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    NOT_FOUND("1000", HttpStatus.BAD_REQUEST, "Record not found"),
    INVALID_ID("2000", HttpStatus.BAD_REQUEST, "Invalid ID"),
    API_FAIL("3000", HttpStatus.BAD_REQUEST, "API FAIL")
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

}