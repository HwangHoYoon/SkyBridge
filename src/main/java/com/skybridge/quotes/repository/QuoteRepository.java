package com.skybridge.quotes.repository;

import com.skybridge.quotes.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}