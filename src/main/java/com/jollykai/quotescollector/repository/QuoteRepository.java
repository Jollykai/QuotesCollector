package com.jollykai.quotescollector.repository;

import com.jollykai.quotescollector.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Iterable<Quote> findAllByOrderByLastModifyDesc();
    List<Quote> findAllByOrderByVotesDesc();
    List<Quote> findAllByOrderByVotesAsc();
}
