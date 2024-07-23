package com.skybridge.log.service;


import com.skybridge.log.dto.ApiLogReq;
import com.skybridge.log.entity.ApiLog;
import com.skybridge.log.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    public void saveLog(ApiLogReq apiLogReq) {
        ApiLog apiLog = new ApiLog();
        apiLog.setUrl(apiLogReq.getUrl());
        apiLog.setReq(apiLogReq.getReq());
        apiLog.setRes(apiLogReq.getRes());
        apiLog.setReqDate(apiLogReq.getReqDate());
        apiLogRepository.save(apiLog);
    }
}
