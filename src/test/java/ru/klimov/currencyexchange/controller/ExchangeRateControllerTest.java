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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExchangeRateControllerTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateController exchangeRateController;

    @Test
    void getExchangeRate_ReturnsValidExchangeRate() {
        // given
        String codePair = "USDGBP";
        ExchangeRate expectedExchangeRate = new ExchangeRate(2L,
                new Currency(4L, "USD", "United States Dollar", "$"),
                new Currency(5L, "GBP", "British Pound", "£"),
                BigDecimal.valueOf(3.15));
        doReturn(expectedExchangeRate).when(this.exchangeRateService).getExchangeRateByCodePair(codePair);

        // when
        var actualExchangeRate = exchangeRateController.getExchangeRate(codePair);

        // then
        assertNotNull(actualExchangeRate);
        assertEquals(expectedExchangeRate, actualExchangeRate);
        verify(this.exchangeRateService).getExchangeRateByCodePair(codePair);
    }

    @Test
    void updateExchangeRate_ReturnsValidExchangeRate() {
        // given
        String codePair = "USDEUR";
        Map<String, String> rateParamMap = new HashMap<>();
        rateParamMap.put("rate", "0.85");
        ExchangeRate expectedExchangeRate = new ExchangeRate(2L,
                new Currency(4L, "USD", "United States Dollar", "$"),
                new Currency(5L, "EUR", "Euro", "€"),
                BigDecimal.valueOf(0.85));
        doReturn(expectedExchangeRate).when(this.exchangeRateService).updateExchangeRate(codePair, rateParamMap);

        // when
        var actualExchangeRate = exchangeRateController.updateExchangeRate(codePair, rateParamMap);

        // then
        assertNotNull(actualExchangeRate);
        assertEquals(expectedExchangeRate, actualExchangeRate);
        verify(this.exchangeRateService).updateExchangeRate(codePair, rateParamMap);
    }
}