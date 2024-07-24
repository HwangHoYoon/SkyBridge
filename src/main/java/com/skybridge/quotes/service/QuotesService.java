package com.skybridge.quotes.service;

import com.skybridge.quotes.dto.QuotesRes;
import com.skybridge.quotes.entity.Quote;
import com.skybridge.quotes.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuotesService {

    private final QuoteRepository quoteRepository;

    public ResponseEntity<List<QuotesRes>> quotes() {
        List<QuotesRes> quotesResList = new ArrayList<>();
        List<Quote> quotes = quoteRepository.findAll();

        if (!quotes.isEmpty()) {
            quotes.forEach(quote -> {
                QuotesRes quotesRes = new QuotesRes();
                quotesRes.setQuote(quote.getQuote());
                quotesRes.setAuthor(quote.getAuthor());
                quotesResList.add(quotesRes);
            });
        }
        return ResponseEntity.ok(quotesResList);
    }
}
