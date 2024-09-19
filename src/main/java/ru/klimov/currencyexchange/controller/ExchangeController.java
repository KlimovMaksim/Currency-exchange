package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.klimov.currencyexchange.response.ExchangeResponse;
import ru.klimov.currencyexchange.service.ExchangeService;

@RestController
public class ExchangeController {

    private final ExchangeService exchangeService;

    @Autowired
    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/exchange")
    public ExchangeResponse exchange(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount) {
        return exchangeService.calculateExchange(from, to, amount);
    }
}
