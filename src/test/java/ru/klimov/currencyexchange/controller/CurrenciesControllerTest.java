package ru.klimov.currencyexchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.service.CurrencyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurrenciesControllerTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrenciesController currenciesController;

    @Test
    void getCurrencies_ReturnsValidCurrencies() {
        // given
        List<Currency> expectedCurrencies = List.of(new Currency(1L, "USD", "United State Dollar", "$"),
                new Currency(2L, "EUR", "Euro", "€"),
                new Currency(3L, "JPY", "Japanese Yen", "¥"));
        doReturn(expectedCurrencies).when(this.currencyService).findAllCurrencies();

        // when
        var actualCurrencies = currenciesController.getCurrencies();

        // then
        assertNotNull(actualCurrencies);
        assertEquals(HttpStatus.OK, actualCurrencies.getStatusCode());
        assertEquals(expectedCurrencies, actualCurrencies.getBody());
        verify(this.currencyService).findAllCurrencies();
    }

    @Test
    void createCurrency_ReturnsValidCurrency() {
        // given
        Map<String, String> currencyParamMap = new HashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("fullname", "United State Dollar");
        currencyParamMap.put("sign", "$");
        Currency expectedCurrency = new Currency(1L, "USD", "United State Dollar", "$");
        doReturn(expectedCurrency).when(this.currencyService).createCurrency(currencyParamMap);

        // when
        var actualCurrency = currenciesController.createCurrency(currencyParamMap);

        //then
        assertNotNull(actualCurrency);
        assertEquals(HttpStatus.CREATED, actualCurrency.getStatusCode());
        assertEquals(expectedCurrency, actualCurrency.getBody());
        verify(this.currencyService).createCurrency(currencyParamMap);
    }
}