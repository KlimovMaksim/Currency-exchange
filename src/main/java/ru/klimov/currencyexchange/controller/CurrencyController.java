package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.service.CurrencyService;

import java.util.Map;

@RestController
@RequestMapping(path = "/currencies", produces = "application/json")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public Iterable<Currency> getCurrencies() {
        return currencyService.findAllCurrencies();
    }

    @GetMapping("/{currencyCode}")
    public Currency getCurrency(@PathVariable String currencyCode) {
        return currencyService.getCurrency(currencyCode);
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.CREATED)
    public Currency createCurrency(@RequestParam Map<String, String> currencyParamMap) {
        return currencyService.createCurrency(currencyParamMap);
    }
}
