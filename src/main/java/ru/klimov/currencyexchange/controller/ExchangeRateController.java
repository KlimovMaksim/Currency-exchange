package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.service.ExchangeRateService;

import java.util.Map;

@RestController
@RequestMapping(path = "/exchangeRate", produces = "application/json")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping({"/{codePair}", "/"})
    public ResponseEntity<ExchangeRate> getExchangeRate(@PathVariable(required = false) String codePair) {
        return new ResponseEntity<>(exchangeRateService.getExchangeRateByCodePair(codePair), HttpStatus.OK);
    }

    @PatchMapping(path = "/{codePair}", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<ExchangeRate> updateExchangeRate(@PathVariable String codePair,
                                           @RequestParam Map<String, String> rateParamMap) {
        return new ResponseEntity<>(exchangeRateService.updateExchangeRate(codePair, rateParamMap), HttpStatus.OK);
    }
}
