package ru.klimov.currencyexchange.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.exceptions.CurrencyAlreadyExistException;
import ru.klimov.currencyexchange.exceptions.CurrencyNotFoundException;
import ru.klimov.currencyexchange.exceptions.InvalidCurrencyDataException;
import ru.klimov.currencyexchange.repository.CurrencyRepository;

import java.util.Map;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final ModelMapper mapper;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository, ModelMapper mapper) {
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
    }

    public Iterable<Currency> findAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Currency getCurrency(String currencyCode) {
        if (currencyCode.isBlank()) throw new InvalidCurrencyDataException("Currency code must not be empty");
        return currencyRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new CurrencyNotFoundException("Currency with code " + currencyCode + " not found"));
    }


    public Currency createCurrency(Map<String, String> currencyParamMap) {
        validateCurrencyData(currencyParamMap);
        Currency currency = mapper.map(currencyParamMap, Currency.class);
        try {
            return currencyRepository.save(currency);
        } catch (DuplicateKeyException e) {
            throw new CurrencyAlreadyExistException("Currency with code " +
                    currencyParamMap.get("code") + " already exists");
        }
    }

    private void validateCurrencyData(Map<String, String> currencyParamMap) {
        if (!currencyParamMap.containsKey("code") || currencyParamMap.get("code").isBlank())
            throw new InvalidCurrencyDataException("Currency code is required and cannot be empty");
        if (!currencyParamMap.containsKey("fullname") || currencyParamMap.get("fullname").isBlank())
            throw new InvalidCurrencyDataException("Currency fullname is required and cannot be empty");
        if (!currencyParamMap.containsKey("sign") || currencyParamMap.get("sign").isBlank())
            throw new InvalidCurrencyDataException("Currency sign is required and cannot be empty");
    }
}
