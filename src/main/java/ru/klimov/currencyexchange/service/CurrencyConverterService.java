package ru.klimov.currencyexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.exceptions.ExchangeRateNotFoundException;
import ru.klimov.currencyexchange.response.ExchangeResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyConverterService {

    private final ExchangeRateService exchangeRateService;
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyConverterService(ExchangeRateService exchangeRateService, CurrencyService currencyService) {
        this.exchangeRateService = exchangeRateService;
        this.currencyService = currencyService;
    }

    private static void validateRequestParam(String from, String to, double amount) {
        if (from.isBlank() || to.isBlank()) throw new IllegalArgumentException("Param from or to are blank");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than 0");
    }

    public ExchangeResponse calculateExchange(String from, String to, double amount) {
        validateRequestParam(from, to, amount);
        Currency fromCurrency = currencyService.getCurrency(from);
        Currency toCurrency = currencyService.getCurrency(to);
        BigDecimal amountBD = new BigDecimal(amount);
        BigDecimal fromToRate = resolveFromToRate(from, to);
        BigDecimal convertedAmount = fromToRate.multiply(amountBD);
        return new ExchangeResponse(
                fromCurrency,
                toCurrency,
                fromToRate,
                amountBD,
                convertedAmount
        );
    }

    private BigDecimal resolveFromToRate(String from, String to) {
        try {
            BigDecimal directExchangeRate = exchangeRateService.getExchangeRateByCodePair(from, to).getRate();
            return directExchangeRate;
        } catch (ExchangeRateNotFoundException ignored) {}
        try {
            BigDecimal reverseExchangeRate = exchangeRateService.getExchangeRateByCodePair(to, from).getRate();
            return BigDecimal.ONE.divide(reverseExchangeRate, 6, RoundingMode.HALF_UP);
        } catch (ExchangeRateNotFoundException ignored) {}
        try {
            BigDecimal fromUsdRate = resolveFromToRate("USD", from);
            BigDecimal toUsdRate = resolveFromToRate("USD", to);
            return toUsdRate.divide(fromUsdRate, 6, RoundingMode.HALF_UP);
        } catch (ExchangeRateNotFoundException ex) {
            throw new ExchangeRateNotFoundException("Exchange rate with " + from + "->" + to + " not found");
        }
    }
}
