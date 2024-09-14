package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.repository.JdbcCurrencyRepository;
import ru.klimov.currencyexchange.repository.JdbcExchangeRateRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/exchangeRates", produces = "application/json")
public class ExchangeRateController {

    private final JdbcExchangeRateRepository exchangeRateRepository;
    private final JdbcCurrencyRepository currencyRepository;

    @Autowired
    public ExchangeRateController(JdbcExchangeRateRepository exchangeRateRepository, JdbcCurrencyRepository currencyRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    public Iterable<ExchangeRate> getExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    @GetMapping("/{codePair}")
    public ResponseEntity<ExchangeRate> getExchangeRate(@PathVariable String codePair) {
        Optional<ExchangeRate> result = exchangeRateRepository.findExchangeRateByCodePair(
                codePair.substring(0, 3),
                codePair.substring(3));
        if (result.isPresent()) {
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ExchangeRate createExchangeRate(@RequestParam Map<String, String> multiValueMap) {
        Currency baseCurrency = currencyRepository.findByCurrencyCode(multiValueMap.get("baseCurrencyCode")).get();
        Currency targetCurrency = currencyRepository.findByCurrencyCode(multiValueMap.get("targetCurrencyCode")).get();
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(new BigDecimal(multiValueMap.get("rate")));
        exchangeRateRepository.save(exchangeRate);
        return exchangeRate;
    }

    @PatchMapping(path = "/{codePair}", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<ExchangeRate> updateExchangeRate(@PathVariable String codePair,
                                           @RequestParam Map<String, String> multiValueMap) {
        Optional<ExchangeRate> result = exchangeRateRepository.findExchangeRateByCodePair(
                codePair.substring(0, 3),
                codePair.substring(3));
        if (result.isPresent()) {
            result.get().setRate(new BigDecimal(multiValueMap.get("rate")));
            exchangeRateRepository.update(result.get());
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
