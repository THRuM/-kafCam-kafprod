package org.app.kafprod.scheduler;

import org.app.domain.Currency;
import org.app.kafprod.api.CurrencyAPI;
import org.app.kafprod.kafka.KafkaCurrencyEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRequestTask {

    private static final Logger log = LoggerFactory.getLogger(CurrencyRequestTask.class);

    private KafkaCurrencyEventProducer kafkaProducer;

    private CurrencyAPI currencyAPI;

    @Autowired
    public CurrencyRequestTask(KafkaCurrencyEventProducer kafkaProducer, CurrencyAPI currencyAPI) {
        this.kafkaProducer = kafkaProducer;
        this.currencyAPI = currencyAPI;
    }

    @Scheduled(fixedRateString = "${request.send.time.ms}")
    public void reportCurrentTime() {

        Currency[] currency = currencyAPI.getCurrencyEvent();

        if(currency != null){
            log.info("Sending {} events to Kafka", currency.length);
            kafkaProducer.send(currency);
        }

    }
}
