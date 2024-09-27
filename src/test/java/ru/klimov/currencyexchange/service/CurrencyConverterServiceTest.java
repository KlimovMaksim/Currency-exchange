package ru.klimov.currencyexchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.exceptions.ExchangeRateNotFoundException;
import ru.klimov.currencyexchange.response.ExchangeResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterServiceTest {

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyConverterService currencyConverterService;

    @Test
    void calculateExchange_ValidDirectRate_SuccessfulCalculation() {
        // given
        String from = "USD";
        String to = "EUR";
        double amount = 100.0;

        Currency fromCurrency = new Currency(1L, "USD", "United States Dollar", "$");
        Currency toCurrency = new Currency(2L, "EUR", "Euro", "€");
        BigDecimal rate = new BigDecimal("0.85");

        when(currencyService.getCurrency("USD")).thenReturn(fromCurrency);
        when(currencyService.getCurrency("EUR")).thenReturn(toCurrency);
        when(exchangeRateService.getExchangeRateByCodePair("USD", "EUR"))
                .thenReturn(new ExchangeRate(1L, fromCurrency, toCurrency, rate));

        // when
        ExchangeResponse response = currencyConverterService.calculateExchange(from, to, amount);

        // then
        assertNotNull(response);
        assertEquals(fromCurrency, response.getBaseCurrency());
        assertEquals(toCurrency, response.getTargetCurrency());
        assertEquals(new BigDecimal("100.0"), response.getAmount());
        assertEquals(new BigDecimal("85.00"), response.getConvertedAmount().setScale(2, RoundingMode.HALF_UP));
        verify(exchangeRateService, times(1)).getExchangeRateByCodePair("USD", "EUR");
    }

    @Test
    void calculateExchange_ReverseRate_SuccessfulCalculation() {
        // given
        String from = "EUR";
        String to = "USD";
        double amount = 100.0;

        Currency fromCurrency = new Currency(1L, "EUR", "Euro", "€");
        Currency toCurrency = new Currency(2L, "USD", "United States Dollar", "$");
        BigDecimal reverseRate = new BigDecimal("0.85");

        when(currencyService.getCurrency("EUR")).thenReturn(fromCurrency);
        when(currencyService.getCurrency("USD")).thenReturn(toCurrency);
        when(exchangeRateService.getExchangeRateByCodePair("EUR", "USD"))
                .thenThrow(ExchangeRateNotFoundException.class);
        when(exchangeRateService.getExchangeRateByCodePair("USD", "EUR"))
                .thenReturn(new ExchangeRate(1L, toCurrency, fromCurrency, reverseRate));

        // when
        ExchangeResponse response = currencyConverterService.calculateExchange(from, to, amount);

        // then
        assertNotNull(response);
        assertEquals(fromCurrency, response.getBaseCurrency());
        assertEquals(toCurrency, response.getTargetCurrency());
        assertEquals(new BigDecimal("100.0"), response.getAmount());
        assertEquals(new BigDecimal("117.65"), response.getConvertedAmount().setScale(2, RoundingMode.HALF_UP));
        verify(exchangeRateService, times(1)).getExchangeRateByCodePair("USD", "EUR");
    }

    @Test
    void calculateExchange_RateThroughUSD_SuccessfulCalculation() {
        // given
        String from = "GBP";
        String to = "JPY";
        double amount = 100.0;

        Currency fromCurrency = new Currency(1L, "GBP", "British Pound", "£");
        Currency toCurrency = new Currency(2L, "JPY", "Japanese Yen", "¥");
        BigDecimal fromUsdRate = new BigDecimal("1.39");
        BigDecimal toUsdRate = new BigDecimal("109.53");

        when(currencyService.getCurrency("GBP")).thenReturn(fromCurrency);
        when(currencyService.getCurrency("JPY")).thenReturn(toCurrency);
        when(exchangeRateService.getExchangeRateByCodePair("GBP", "JPY"))
                .thenThrow(ExchangeRateNotFoundException.class);
        when(exchangeRateService.getExchangeRateByCodePair("JPY", "GBP"))
                .thenThrow(ExchangeRateNotFoundException.class);
        when(exchangeRateService.getExchangeRateByCodePair("USD", "GBP"))
                .thenReturn(new ExchangeRate(1L, new Currency(3L, "USD", "Dollar", "$"), fromCurrency, fromUsdRate));
        when(exchangeRateService.getExchangeRateByCodePair("USD", "JPY"))
                .thenReturn(new ExchangeRate(2L, new Currency(3L, "USD", "Dollar", "$"), toCurrency, toUsdRate));

        // when
        ExchangeResponse response = currencyConverterService.calculateExchange(from, to, amount);

        // then
        assertNotNull(response);
        assertEquals(fromCurrency, response.getBaseCurrency());
        assertEquals(toCurrency, response.getTargetCurrency());
        assertEquals(new BigDecimal("100.0"), response.getAmount());
        assertEquals(new BigDecimal("7879.86"), response.getConvertedAmount().setScale(2, RoundingMode.HALF_UP));
        verify(exchangeRateService, times(1)).getExchangeRateByCodePair("USD", "GBP");
        verify(exchangeRateService, times(1)).getExchangeRateByCodePair("USD", "JPY");
    }

    @Test
    void calculateExchange_InvalidAmount_ThrowsIllegalArgumentException() {
        // given
        String from = "USD";
        String to = "EUR";
        double invalidAmount = -50.0;

        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> currencyConverterService.calculateExchange(from, to, invalidAmount));

        // then
        assertEquals("Amount must be greater than 0", exception.getMessage());
        verify(exchangeRateService, never()).getExchangeRateByCodePair(anyString(), anyString());
    }

    @Test
    void calculateExchange_ExchangeRateNotFound_ThrowsExchangeRateNotFoundException() {
        // given
        String from = "USD";
        String to = "EUR";
        double amount = 100.0;

        when(currencyService.getCurrency(from)).thenReturn(new Currency(1L, "USD", "United States Dollar", "$"));
        when(currencyService.getCurrency(to)).thenReturn(new Currency(2L, "EUR", "Euro", "€"));
        when(exchangeRateService.getExchangeRateByCodePair("USD", "EUR"))
                .thenThrow(new ExchangeRateNotFoundException("Exchange rate not found"));
        when(exchangeRateService.getExchangeRateByCodePair("EUR", "USD"))
                .thenThrow(new ExchangeRateNotFoundException("Exchange rate not found"));
        when(exchangeRateService.getExchangeRateByCodePair("USD", "USD"))
                .thenThrow(new ExchangeRateNotFoundException("Exchange rate not found"));

        // when
        Exception exception = assertThrows(ExchangeRateNotFoundException.class,
                () -> currencyConverterService.calculateExchange(from, to, amount));

        // then
        assertEquals("Exchange rate with USD->EUR not found", exception.getMessage());
        verify(exchangeRateService, times(1)).getExchangeRateByCodePair("USD", "EUR");
    }

    @Test
    void calculateExchange_BlankFromParam_ThrowsIllegalArgumentException() {
        // given
        String from = "   "; // blank
        String to = "EUR";
        double amount = 100.0;

        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> currencyConverterService.calculateExchange(from, to, amount));

        // then
        assertEquals("Param from or to are blank", exception.getMessage());
        verify(exchangeRateService, never()).getExchangeRateByCodePair(anyString(), anyString());
    }
}