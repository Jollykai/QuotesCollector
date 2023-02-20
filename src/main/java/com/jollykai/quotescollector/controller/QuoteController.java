package com.jollykai.quotescollector.controller;

import com.jollykai.quotescollector.entity.Quote;
import com.jollykai.quotescollector.entity.User;
import com.jollykai.quotescollector.repository.QuoteRepository;
import com.jollykai.quotescollector.repository.UserRepository;
import com.jollykai.quotescollector.service.QuoteService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/quotes")
public class QuoteController {

    private final QuoteService quoteService;
    private final UserRepository userRepository;
    private final QuoteRepository quoteRepository;

    public QuoteController(@Autowired QuoteService quoteService, @Autowired UserRepository userRepository, @Autowired QuoteRepository quoteRepository) {
        this.quoteService = quoteService;
        this.userRepository = userRepository;
        this.quoteRepository = quoteRepository;
    }

    @PostMapping
    public ResponseEntity<?> createQuote(@RequestBody Quote quote) {
        if (!userRepository.existsById(quote.getUserId())) {//
            return new ResponseEntity<>(JSONObject.quote("User with referenced Id not exist"), HttpStatus.FORBIDDEN);
        }
        Quote createdQuote = quoteService.createQuote(quote);
        return ResponseEntity.ok(createdQuote);
    }

    @GetMapping()
    public ResponseEntity<Iterable<Quote>> showLastQuotes() {
        return ResponseEntity.ok(quoteRepository.findAllByOrderByLastModifyDesc());
    }

    @GetMapping("/random")
    public ResponseEntity<Quote> getRandomQuote() {
        Quote randomQuote = quoteService.getRandomQuote();
        return ResponseEntity.ok(randomQuote);
    }

    @GetMapping("/top-10")
    public ResponseEntity<Iterable<Quote>> getTopTenQuotes() {
        List<Quote> topTenQuotes = quoteService.getTopTenQuotes();
        return ResponseEntity.ok(topTenQuotes);
    }

    @GetMapping("/flop-10")
    public ResponseEntity<Iterable<Quote>> getWorstTenQuotes() {
        List<Quote> worstTenQuotes = quoteService.getWorstTenQuotes();
        return ResponseEntity.ok(worstTenQuotes);
    }

    @GetMapping("/{quoteId}")
    public ResponseEntity<?> getQuoteById(@PathVariable Long quoteId) {
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        if (quote.isPresent()) {
            return ResponseEntity.ok(quote.get());
        }
        return new ResponseEntity<>(JSONObject.quote("Quote with referenced Id not exist"), HttpStatus.FORBIDDEN);
    }

    @PutMapping("/{quoteId}")
    public ResponseEntity<?> updateQuote(@PathVariable Long quoteId, @RequestBody Quote updatedQuote) {
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        if (quote.isPresent()) {
            Quote renewedQuote = quoteService.updateQuote(quote.get(), updatedQuote);
            return ResponseEntity.ok(renewedQuote);
        }
        return new ResponseEntity<>(JSONObject.quote("Quote with referenced Id not exist"), HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{quoteId}")
    public ResponseEntity<?> deleteQuote(@PathVariable Long quoteId) {
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        if (quote.isPresent()) {
            quoteRepository.deleteById(quoteId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(JSONObject.quote("Quote with referenced Id not exist"), HttpStatus.FORBIDDEN);
    }

    @PostMapping("/{quoteId}/votes")
    public ResponseEntity<?> countVotes(@PathVariable Long quoteId, @RequestBody Quote vote) {
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        Optional<User> user = userRepository.findById(vote.getUserId());
        if (quote.isEmpty()) {
            return new ResponseEntity<>(JSONObject.quote("Quote with referenced Id not exist"), HttpStatus.FORBIDDEN);
        }
        if (user.isEmpty()) {
            return new ResponseEntity<>(JSONObject.quote("User with referenced Id not exist"), HttpStatus.FORBIDDEN);
        }
        if (vote.getVotes() != 1 && vote.getVotes() != -1) {
            return new ResponseEntity<>(JSONObject.quote("Incorrect vote value"), HttpStatus.BAD_REQUEST);
        }
        if (Objects.equals(user.get().getId(), quote.get().getUserId())) {
            return new ResponseEntity<>(JSONObject.quote("An author can't vote for his own quote"), HttpStatus.FORBIDDEN);
        }

        List<Long> quotedList = user.get().getVotedQuotes();
        if (quotedList.contains(quote.get().getId())) {
            return new ResponseEntity<>(JSONObject.quote("This quote was already voted"), HttpStatus.FORBIDDEN);
        }

        Quote updatedQuote = quoteService.updateVotes(quote.get(), vote);
        quotedList.add(updatedQuote.getId());
        userRepository.save(user.get());
        return ResponseEntity.ok(updatedQuote);
    }
}
