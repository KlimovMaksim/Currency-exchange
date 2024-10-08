package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.klimov.currencyexchange.response.ExchangeResponse;
import ru.klimov.currencyexchange.service.CurrencyConverterService;

@RestController
public class ExchangeController {

    private final CurrencyConverterService currencyConverterService;

    @Autowired
    public ExchangeController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }

    @GetMapping("/exchange")
    public ResponseEntity<ExchangeResponse> exchange(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount) {
        return new ResponseEntity<>(currencyConverterService.calculateExchange(from, to, amount), HttpStatus.OK);
    }
}
