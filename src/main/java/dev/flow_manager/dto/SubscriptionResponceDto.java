package dev.flow_manager.dto;

import dev.flow_manager.enums.SubscriptionStatus;

import java.time.OffsetDateTime;

public record SubscriptionResponceDto(
        String userName,
        SubscriptionStatus status,
        OffsetDateTime expiresAt
){
}
