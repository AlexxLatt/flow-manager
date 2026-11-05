package dev.flow_manager.event;

import java.util.UUID;

public record FileConversionResultEvent(
        String eventId,
        UUID eventKey,
        String status,
        String resultFilePath,
        String errorMessage
)
{}
