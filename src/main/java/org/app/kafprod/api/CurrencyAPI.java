package org.app.kafprod.api;

import org.app.domain.Currency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Component
public class CurrencyAPI {

    @Value("${api.url}")
    private String templateURL;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.currency}")
    private String currencySymbol;

    private String apiURL;

    @PostConstruct
    private void init() {
        apiURL = String.format(templateURL, currencySymbol, apiKey);
    }

    private RestTemplate restTemplate = new RestTemplate();

    public Currency[] getCurrencyEvent() {
        return restTemplate.getForObject(apiURL, Currency[].class);
    }
}
