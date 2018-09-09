package org.app.kafprod.kafka;

import org.app.domain.Currency;
import org.app.domain.event.ProducerRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;

@Component
public class KafkaCurrencyEventProducer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaCurrencyEventProducer.class);

    private KafkaTemplate<String, Currency> kafkaTemplate;
    private KafkaTemplate<String, ProducerRegisteredEvent> registerTemplate;
    private Environment env;

    @Autowired
    public KafkaCurrencyEventProducer(KafkaTemplate<String, Currency> kafkaTemplate,
                                      KafkaTemplate<String, ProducerRegisteredEvent> registerTemplate,
                                      Environment env) {
        this.kafkaTemplate = kafkaTemplate;
        this.registerTemplate = registerTemplate;
        this.env = env;
    }

    @Value("${topic.currency}")
    private String currencyTopic;

    @Value("${application.topic}")
    private String applicationTopic;

    @PostConstruct
    public void initRegister() {
        String producerId = env.getRequiredProperty("spring.kafka.producer.client-id");
        String currencies = env.getRequiredProperty("api.currency");
        Long interval = Long.valueOf(env.getRequiredProperty("request.send.time.ms"));

        ProducerRegisteredEvent producerRegisteredEvent = new ProducerRegisteredEvent(producerId, currencies, interval);

        LOG.info("Sending registering event for producerId {}", producerId);

        send(producerRegisteredEvent);
    }

    public void send(Currency... events) {
        LOG.info("sending message to currencyTopic='{}'", currencyTopic);

        Arrays.stream(events).filter(Objects::nonNull).forEach(currency -> kafkaTemplate.send(currencyTopic, currency.getSymbol(), currency));
    }

    private void send(ProducerRegisteredEvent producerRegisteredEvent) {
        LOG.info("sending registering event to currencyTopic ='{}'", currencyTopic);

        registerTemplate.send(applicationTopic, producerRegisteredEvent.getProducerId(), producerRegisteredEvent);
    }
}
