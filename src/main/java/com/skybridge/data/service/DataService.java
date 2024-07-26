package com.skybridge.data.service;

import com.skybridge.data.dto.DataRes;
import com.skybridge.data.entity.AdmDatum;
import com.skybridge.data.repository.AdmDatumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class DataService {
    private final AdmDatumRepository admDatumRepository;

    public ResponseEntity<List<DataRes>> data() {
        List<DataRes> dataResList = new ArrayList<>();

        List<AdmDatum> admDataList = admDatumRepository.findAll();

        if (!admDataList.isEmpty()) {
            admDataList.forEach(admDatum -> {
                DataRes dataRes = new DataRes();
                dataRes.setId(admDatum.getId());
                dataRes.setDataName(admDatum.getDataName());
                dataRes.setDataLink(admDatum.getDataLink());
                dataRes.setViewCount(admDatum.getViewCount());
                dataResList.add(dataRes);
            });
        }

        return ResponseEntity.ok(dataResList);
    }
}
