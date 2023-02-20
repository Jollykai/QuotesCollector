package com.jollykai.quotescollector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {

    @Bean
    public Clock clock(@Value("${server.time-zone}") String timeZone) {
        return Clock.system(ZoneId.of(timeZone));
    }
}
