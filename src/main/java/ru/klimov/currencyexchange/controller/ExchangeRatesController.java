package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Iterable<ExchangeRate>> getExchangeRates() {
        return new ResponseEntity<>(exchangeRateService.findAllExchangeRates(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<ExchangeRate> createExchangeRate(@RequestParam Map<String, String> exchangeRateParamMap) {
        return new ResponseEntity<>(exchangeRateService.createExchangeRate(exchangeRateParamMap), HttpStatus.CREATED);
    }
}
