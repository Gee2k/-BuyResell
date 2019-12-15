package de.gx.buyresell.io.util.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Configuration
public class ResilienceConfig {
    CircuitBreakerRegistry circuitBreakerRegistry;
    CircuitBreakerConfig circuitBreakerConfig;

    public ResilienceConfig() {
        circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowSize(2)
                .recordExceptions(IOException.class, TimeoutException.class, RuntimeException.class)
//                .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
                .build();

        circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

    @Bean
    public CircuitBreaker circuitBreaker(){
        return CircuitBreaker.of("circuitBreaker", circuitBreakerConfig);
    }
}
