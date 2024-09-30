package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.service.CurrencyService;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(path = "/currencies", produces = "application/json")
public class CurrenciesController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrenciesController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<Iterable<Currency>> getCurrencies() {
        return new ResponseEntity<>(currencyService.findAllCurrencies(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Currency> createCurrency(@RequestParam Map<String, String> currencyParamMap) {
        return new ResponseEntity<>(currencyService.createCurrency(currencyParamMap), HttpStatus.CREATED);
    }
}
