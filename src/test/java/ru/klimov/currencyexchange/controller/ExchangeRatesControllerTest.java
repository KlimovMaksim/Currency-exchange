package ru.klimov.currencyexchange.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.service.ExchangeRateService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesControllerTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRatesController exchangeRatesController;

    @Test
    void getExchangeRate_ReturnsValidExchangeRate() {
        // given
        List<ExchangeRate> expectedExchangeRate = List.of(
                new ExchangeRate(2L,
                        new Currency(4L, "USD", "United States Dollar", "$"),
                        new Currency(5L, "GBP", "British Pound", "£"),
                        BigDecimal.valueOf(3.15)),
                new ExchangeRate(3L,
                        new Currency(4L, "USD", "United States Dollar", "$"),
                        new Currency(6L, "RUB", "Ruble", "₽"),
                        BigDecimal.valueOf(100.15))
        );
        doReturn(expectedExchangeRate).when(this.exchangeRateService).findAllExchangeRates();

        // when
        var actualExchangeRate = exchangeRatesController.getExchangeRates();

        // then
        assertNotNull(actualExchangeRate);
        assertEquals(expectedExchangeRate, actualExchangeRate);
        verify(this.exchangeRateService).findAllExchangeRates();
    }

    @Test
    void createExchangeRate_ReturnsValidExchangeRate() {
        // given
        ExchangeRate expectedExchangeRate = new ExchangeRate(2L,
                new Currency(1L, "USD", "United States Dollar", "$"),
                new Currency(2L, "EUR", "Euro", "€"),
                BigDecimal.valueOf(0.85));
        Map<String, String> exchangeRateParamMap = new HashMap<>();
        exchangeRateParamMap.put("baseCurrencyCode", "USD");
        exchangeRateParamMap.put("targetCurrencyCode", "EUR");
        exchangeRateParamMap.put("rate", "0.85");;
        doReturn(expectedExchangeRate).when(this.exchangeRateService).createExchangeRate(exchangeRateParamMap);

        // when
        var actualExchangeRate = exchangeRatesController.createExchangeRate(exchangeRateParamMap);

        // then
        assertNotNull(actualExchangeRate);
        assertEquals(expectedExchangeRate, actualExchangeRate);
        verify(this.exchangeRateService).createExchangeRate(exchangeRateParamMap);
    }
}