package com.skybridge.tmp.service;

import com.skybridge.tmp.entity.Tmp;
import com.skybridge.tmp.repository.TmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TmpService {


    private final TmpRepository tmpRepository;


    public List<Tmp> selectTmp() {
        return tmpRepository.findAll();
    }
}
