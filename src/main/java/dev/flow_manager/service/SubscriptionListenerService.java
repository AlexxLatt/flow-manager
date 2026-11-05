package dev.flow_manager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SubscriptionListenerService {
    private final CacheManager cacheManager;

    public void updateStatus(String userName) {

        Cache cache = cacheManager.getCache("subscription-status");

        if (cache != null) {
            cache.evict(userName);
        }
    }
}
