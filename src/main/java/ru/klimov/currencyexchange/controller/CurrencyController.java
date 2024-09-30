package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.service.CurrencyService;

@RestController
@RequestMapping(path = "/currency", produces = "application/json")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/{currencyCode}")
    public ResponseEntity<Currency> getCurrency(@PathVariable String currencyCode) {
        return new ResponseEntity<>(currencyService.getCurrency(currencyCode), HttpStatus.OK);
    }
}
