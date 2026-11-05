package dev.flow_manager.exception;

public class SubscriptionRequiredException extends RuntimeException {
    public SubscriptionRequiredException(String message) {
        super(message);
    }
}
