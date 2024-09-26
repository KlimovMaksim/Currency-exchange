package ru.klimov.currencyexchange.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.exceptions.ExchangeRateAlreadyExistsException;
import ru.klimov.currencyexchange.exceptions.ExchangeRateNotFoundException;
import ru.klimov.currencyexchange.exceptions.InvalidExchangeRateDataException;
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
        validateCodePair(codePair);
        return findExchangeRate(codePair.substring(0, 3), codePair.substring(3));
    }

    public ExchangeRate getExchangeRateByCodePair(String base, String target) {
        return getExchangeRateByCodePair(base + target);
    }

    private ExchangeRate findExchangeRate(String base, String target) {
        return exchangeRateRepository.findExchangeRateByCodePair(base, target)
                .orElseThrow(() -> new ExchangeRateNotFoundException("Exchange rate with " + base + "->" + target + " not found"));
    }

    public ExchangeRate createExchangeRate(Map<String, String> exchangeRateParamMap) {
        validateExchangeRateParams(exchangeRateParamMap);
        return saveExchangeRate(exchangeRateParamMap);
    }

    public ExchangeRate updateExchangeRate(String codePair, Map<String, String> rateParamMap) {
        validateCodePair(codePair);
        validateRateParam(rateParamMap);
        ExchangeRate exchangeRate = getExchangeRateByCodePair(codePair);
        exchangeRate.setRate(new BigDecimal(rateParamMap.get("rate")));
        exchangeRateRepository.update(exchangeRate);
        return exchangeRate;
    }

    private void validateCodePair(String codePair) {
        if (codePair == null || codePair.isBlank()) {
            throw new InvalidExchangeRateDataException("Currency codes must not be empty");
        }
        if (codePair.trim().length() != 6) {
            throw new InvalidExchangeRateDataException("Currency base and target codes must contain 3 characters each");
        }
    }

    private void validateExchangeRateParams(Map<String, String> params) {
        validateCodePair(params.get("baseCurrencyCode") + params.get("targetCurrencyCode"));
        validateRateParam(params);
    }

    private void validateRateParam(Map<String, String> rateParamMap) {
        String rate = rateParamMap.get("rate");
        if (rate == null || rate.isBlank()) {
            throw new InvalidExchangeRateDataException("Rate is required and cannot be empty");
        }
    }

    private ExchangeRate saveExchangeRate(Map<String, String> exchangeRateParamMap) {
        Currency baseCurrency = currencyService.getCurrency(exchangeRateParamMap.get("baseCurrencyCode"));
        Currency targetCurrency = currencyService.getCurrency(exchangeRateParamMap.get("targetCurrencyCode"));

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(new BigDecimal(exchangeRateParamMap.get("rate")));

        try {
            return exchangeRateRepository.save(exchangeRate);
        } catch (DuplicateKeyException e) {
            throw new ExchangeRateAlreadyExistsException("Exchange rate " + baseCurrency.getCode() + "->" + targetCurrency.getCode() + " already exists");
        }
    }
}
