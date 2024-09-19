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
//        Optional<ExchangeRate> result = exchangeRateService.findExchangeRateByCodePair(
//                codePair.substring(0, 3),
//                codePair.substring(3));
//        if (result.isPresent()) {
//            return new ResponseEntity<>(result.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
        return exchangeRateService.getExchangeRateByCodePair(codePair);
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ExchangeRate createExchangeRate(@RequestParam Map<String, String> exchangeRateParamMap) {
//        Currency baseCurrency = currencyService.findByCurrencyCode(multiValueMap.get("baseCurrencyCode")).get();
//        Currency targetCurrency = currencyService.findByCurrencyCode(multiValueMap.get("targetCurrencyCode")).get();
//        ExchangeRate exchangeRate = new ExchangeRate();
//        exchangeRate.setBaseCurrency(baseCurrency);
//        exchangeRate.setTargetCurrency(targetCurrency);
//        exchangeRate.setRate(new BigDecimal(multiValueMap.get("rate")));
//        exchangeRateService.save(exchangeRate);
        return exchangeRateService.createExchangeRate(exchangeRateParamMap);
    }

    @PatchMapping(path = "/{codePair}", consumes = "application/x-www-form-urlencoded")
    public ExchangeRate updateExchangeRate(@PathVariable String codePair,
                                           @RequestParam Map<String, String> rateParamMap) {
//        Optional<ExchangeRate> result = exchangeRateService.findExchangeRateByCodePair(
//                codePair.substring(0, 3),
//                codePair.substring(3));
//        if (result.isPresent()) {
//            result.get().setRate(new BigDecimal(multiValueMap.get("rate")));
//            exchangeRateService.update(result.get());
//            return new ResponseEntity<>(result.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
        return exchangeRateService.updateExchangeRate(codePair, rateParamMap);
    }
}
