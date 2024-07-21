package com.skybridge.tmp.repository;

import com.skybridge.tmp.entity.Tmp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmpRepository extends JpaRepository<Tmp, Long> {
}