package com.skybridge.sky.service;

import com.skybridge.sky.entity.SkyResult;
import com.skybridge.sky.repository.SkyResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkyResultService {
    private final SkyResultRepository skyResultRepository;

    public void saveResult(SkyResult skyResult) {
        skyResultRepository.save(skyResult);
    }

    public SkyResult getResult(Long resultId) {
        Optional<SkyResult> result = skyResultRepository.findById(resultId);
        return result.orElseGet(SkyResult::new);
    }
}
