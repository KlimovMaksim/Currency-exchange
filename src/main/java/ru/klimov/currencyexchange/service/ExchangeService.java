package ru.klimov.currencyexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.response.ExchangeResponse;

import java.math.BigDecimal;

@Service
public class ExchangeService {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public ExchangeResponse calculateExchange(String from, String to, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than 0");
        ExchangeRate exchangeRate = resolveExchangeRate(from, to);
        BigDecimal convertedAmount = exchangeRate.getRate().multiply(new BigDecimal(amount));
        return new ExchangeResponse(
                exchangeRate.getBaseCurrency(),
                exchangeRate.getTargetCurrency(),
                exchangeRate.getRate(),
                new BigDecimal(amount),
                convertedAmount
        );
    }

    private ExchangeRate resolveExchangeRate(String from, String to) {
        // todo add variants of find exchange rate
        return exchangeRateService.getExchangeRateByCodePair(from, to);
    }
}
