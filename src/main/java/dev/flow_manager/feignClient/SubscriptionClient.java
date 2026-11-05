package dev.flow_manager.feignClient;

import dev.flow_manager.dto.SubscriptionResponceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "subscription-service")
public interface SubscriptionClient {

    @GetMapping("/api/v1/subscriptions/{userName}")
    SubscriptionResponceDto getSubscriptions(@PathVariable("userName") String userName);
}