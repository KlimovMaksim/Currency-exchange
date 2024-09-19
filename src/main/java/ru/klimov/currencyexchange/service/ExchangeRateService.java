package ru.klimov.currencyexchange.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.exceptions.ExchangeRateAlreadyExistsException;
import ru.klimov.currencyexchange.exceptions.ExchangeRateNotFoundException;
import ru.klimov.currencyexchange.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyService currencyService;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, CurrencyService currencyService) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyService = currencyService;
    }

    public Iterable<ExchangeRate> findAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public ExchangeRate getExchangeRateByCodePair(String codePair) {
        if (codePair.isBlank()) throw new IllegalArgumentException("Exchange rate code pair must not be empty");
        return findExchangeRate(codePair.substring(0, 3), codePair.substring(3));
    }

    private ExchangeRate findExchangeRate(String from, String to) {
        return exchangeRateRepository.findExchangeRateByCodePair(from, to)
                .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange rate with " + from + "->" + to + " not found"));
    }

    public ExchangeRate getExchangeRateByCodePair(String from, String to) {
        if (from.isBlank() || to.isBlank())
            throw new IllegalArgumentException("Exchange rate code pair must not be empty");
        return findExchangeRate(from, to);
    }

    public ExchangeRate createExchangeRate(Map<String, String> exchangeRateParamMap) {
        validateExchangeRate(exchangeRateParamMap);
        try {
            return exchangeRateRepository.save(mapToExchangeRate(exchangeRateParamMap));
        } catch (DuplicateKeyException e) {
            throw new ExchangeRateAlreadyExistsException("Exchange rate " +
                    exchangeRateParamMap.get("baseCurrencyCode") + "->" +
                    exchangeRateParamMap.get("targetCurrencyCode") + " already exists");
        }
    }

    private void validateExchangeRate(Map<String, String> exchangeRateParamMap) {
        if (!exchangeRateParamMap.containsKey("baseCurrencyCode") || exchangeRateParamMap.get("baseCurrencyCode").isBlank())
            throw new IllegalArgumentException("baseCurrencyCode is required and cannot be empty");
        if (!exchangeRateParamMap.containsKey("targetCurrencyCode") || exchangeRateParamMap.get("targetCurrencyCode").isBlank())
            throw new IllegalArgumentException("targetCurrencyCode is required and cannot be empty");
        if (!exchangeRateParamMap.containsKey("rate") || exchangeRateParamMap.get("rate").isBlank())
            throw new IllegalArgumentException("rate is required and cannot be empty");
    }

    private ExchangeRate mapToExchangeRate(Map<String, String> multiValueMap) {
        Currency baseCurrency = currencyService.getCurrency(multiValueMap.get("baseCurrencyCode"));
        Currency targetCurrency = currencyService.getCurrency(multiValueMap.get("targetCurrencyCode"));
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(new BigDecimal(multiValueMap.get("rate")));
        return exchangeRate;
    }

    public ExchangeRate updateExchangeRate(String codePair, Map<String, String> rateParamMap) {
        validateRate(rateParamMap);
        ExchangeRate exchangeRate = getExchangeRateByCodePair(codePair);
        exchangeRateRepository.update(exchangeRate);
        return exchangeRate;
    }

    private void validateRate(Map<String, String> rateParamMap) {
        if (!rateParamMap.containsKey("rate") || rateParamMap.get("rate").isBlank())
            throw new IllegalArgumentException("rate is required and cannot be empty");
    }
}
