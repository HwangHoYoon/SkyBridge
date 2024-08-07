package com.skybridge.news.repository;

import com.skybridge.news.entity.AdmNew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AdmNewRepository extends JpaRepository<AdmNew, Long> {

    List<AdmNew> findByCodeIn(Collection<String> codes);

    long countByCode(String code);


}