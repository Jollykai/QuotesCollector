package com.jollykai.quotescollector.service;

import com.jollykai.quotescollector.entity.Quote;
import com.jollykai.quotescollector.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final Clock clock;

    public QuoteService(@Autowired QuoteRepository quoteRepository, @Autowired Clock clock) {
        this.quoteRepository = quoteRepository;
        this.clock = clock;
    }

    public Quote createQuote(Quote quote) {
        quote.setLastModify(LocalDateTime.now(clock));
        return quoteRepository.save(quote);
    }

    public Quote getRandomQuote() {
        long counter = quoteRepository.count();
        int index = (int) (Math.random() * counter);
        Page<Quote> quotePage = quoteRepository.findAll(PageRequest.of(index, 1));
        Quote randomQuote = null;
        if (quotePage.hasContent()) {
            randomQuote = quotePage.getContent().get(0);
        }
        return randomQuote;
    }

    public List<Quote> getTopTenQuotes() {
        List<Quote> quoteList = quoteRepository.findAllByOrderByVotesDesc();
        int subListSize = Math.min(quoteList.size(), 10);
        return quoteList.subList(0, subListSize);
    }

    public List<Quote> getWorstTenQuotes() {
        List<Quote> quoteList = quoteRepository.findAllByOrderByVotesAsc();
        int subListSize = Math.min(quoteList.size(), 10);
        return quoteList.subList(0, subListSize);
    }

    public Quote updateQuote(Quote originalQuote, Quote updatedQuote) {
        originalQuote.setContent(updatedQuote.getContent());
        originalQuote.setLastModify(LocalDateTime.now(clock));
        return quoteRepository.save(originalQuote);
    }

    public Quote updateVotes(Quote originalQuote, Quote vote) {
        originalQuote.setVotes(originalQuote.getVotes() + vote.getVotes());
        return quoteRepository.save(originalQuote);
    }
}
