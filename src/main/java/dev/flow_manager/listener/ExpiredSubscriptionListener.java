package dev.flow_manager.listener;



import dev.flow_manager.service.SubscriptionListenerService;
import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ExpiredSubscriptionListener {
    private static final String TOPIC_NAME = "expired-subscriptions";
    private final SubscriptionListenerService subscriptionListenerService;

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = "flow-manager-subscription-cache",
            containerFactory = "subscriptionCacheKafkaListenerContainerFactory"
    )
    public void consume(String userName) {
        subscriptionListenerService.updateStatus(userName);

    }
}
