package dev.flow_manager.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;

@Configuration
public class SubscriptionCacheKafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    subscriptionCacheKafkaListenerContainerFactory() {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();

        var props = new HashMap<String, Object>();
        props.put("bootstrap.servers", "file-conversion-kafka:9092");
        props.put("auto.offset.reset", "earliest");
        props.put("group.id", "flow-manager-subscription-cache");
        props.put("enable.auto.commit", false);

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