package dev.flow_manager.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;

@Configuration
public class SubscriptionCacheKafkaConfig {

    @Value("${app.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${app.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${app.kafka.consumer.group-id}")
    private String groupId;

    @Value("${app.kafka.consumer.enable-auto-commit}")
    private boolean enableAutoCommit;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    subscriptionCacheKafkaListenerContainerFactory() {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();

        var props = new HashMap<String, Object>();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("auto.offset.reset", autoOffsetReset);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", enableAutoCommit);

        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new StringDeserializer()
                )
        );

        return factory;
    }
}