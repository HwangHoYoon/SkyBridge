package com.skybridge.log.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApiLogReq {

    private String url;

    private String req;

    private String res;

    private LocalDate reqDate;
}
