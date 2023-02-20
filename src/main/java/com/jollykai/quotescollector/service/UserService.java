package com.jollykai.quotescollector.service;

import com.jollykai.quotescollector.entity.User;
import com.jollykai.quotescollector.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Clock clock;

    public UserService(@Autowired UserRepository userRepository, @Autowired Clock clock) {
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public User createUser(User user){
        user.setCreationDate(LocalDateTime.now(clock));
        return userRepository.save(user);
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }
}
