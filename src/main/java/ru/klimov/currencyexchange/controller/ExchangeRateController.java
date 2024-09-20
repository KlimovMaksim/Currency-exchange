package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.service.ExchangeRateService;

import java.util.Map;

@RestController
@RequestMapping(path = "/exchangeRates", produces = "application/json")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public Iterable<ExchangeRate> getExchangeRates() {
        return exchangeRateService.findAllExchangeRates();
    }

    @GetMapping("/{codePair}")
    public ExchangeRate getExchangeRate(@PathVariable String codePair) {
        return exchangeRateService.getExchangeRateByCodePair(codePair);
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ExchangeRate createExchangeRate(@RequestParam Map<String, String> exchangeRateParamMap) {
        return exchangeRateService.createExchangeRate(exchangeRateParamMap);
    }

    @PatchMapping(path = "/{codePair}", consumes = "application/x-www-form-urlencoded")
    public ExchangeRate updateExchangeRate(@PathVariable String codePair,
                                           @RequestParam Map<String, String> rateParamMap) {
        return exchangeRateService.updateExchangeRate(codePair, rateParamMap);
    }
}
