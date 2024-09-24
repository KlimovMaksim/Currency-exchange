package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.service.ExchangeRateService;

import java.util.Map;

@RestController
@RequestMapping(path = "/exchangeRates", produces = "application/json")
public class ExchangeRatesController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRatesController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public Iterable<ExchangeRate> getExchangeRates() {
        return exchangeRateService.findAllExchangeRates();
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ExchangeRate createExchangeRate(@RequestParam Map<String, String> exchangeRateParamMap) {
        return exchangeRateService.createExchangeRate(exchangeRateParamMap);
    }
}
