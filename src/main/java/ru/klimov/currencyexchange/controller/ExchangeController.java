package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.repository.ExchangeRateRepository;
import ru.klimov.currencyexchange.response.ExchangeResponse;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class ExchangeController {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeController(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @GetMapping("/exchange")
    public ResponseEntity<?> exchange(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount) {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findExchangeRateByCodePair(from, to);
        if (exchangeRate.isPresent()) {
            BigDecimal convertedAmount = exchangeRate.get().getRate().multiply(new BigDecimal(amount));
            ExchangeResponse exchangeResponse = new ExchangeResponse(
                    exchangeRate.get().getBaseCurrency(),
                    exchangeRate.get().getTargetCurrency(),
                    exchangeRate.get().getRate(),
                    new BigDecimal(amount),
                    convertedAmount
            );
            return new ResponseEntity<>(exchangeResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
