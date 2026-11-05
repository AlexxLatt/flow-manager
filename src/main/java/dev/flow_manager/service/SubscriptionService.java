package dev.flow_manager.service;

import dev.flow_manager.dto.SubscriptionResponceDto;
import dev.flow_manager.enums.SubscriptionStatus;
import dev.flow_manager.exception.NotFoundException;
import dev.flow_manager.feignClient.SubscriptionClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@Slf4j
@EnableFeignClients(clients = SubscriptionClient.class)
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionClient subscriptionClient;


    @Cacheable(value = "subscription-status", key = "#userName")
    @CircuitBreaker(name = "subscriptionService", fallbackMethod = "checkStatusFallback")
    public boolean checkStatus(String userName) {
        log.info("checkStatus запустился");

        SubscriptionResponceDto subscriptionResponseDto = subscriptionClient.getSubscriptions(userName);
        log.info("subscriptionResponseDto: {}", subscriptionResponseDto);


        return subscriptionResponseDto.status() == SubscriptionStatus.PAID;

    }

    boolean checkStatusFallback(String userName, Throwable ex) {

        log.error("Subscription Service недоступен для пользователя {}", userName, ex);

        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Subscription Service недоступен", ex);
    }

    @CacheEvict(
            value = "subscription-status",
            key = "#userName"
    )
    public void expireSubscription(String userName) {
        log.info("Expire subscription #{}", userName);
    }

}
