package ru.klimov.currencyexchange.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.response.ExchangeResponse;
import ru.klimov.currencyexchange.service.CurrencyConverterService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExchangeControllerTest {

    @Mock
    private CurrencyConverterService currencyConverterService;

    @InjectMocks
    private ExchangeController exchangeController;

    @Test
    void exchange_ReturnValidExchange() {
        // given
        String from = "USD";
        String to = "EUR";
        double amount = 100;
        ExchangeResponse expectedResponse = new ExchangeResponse(
                new Currency(1L, "USD", "United State Dollar", "$"),
                new Currency(2L, "EUR", "Euro", "€"),
                BigDecimal.valueOf(0.85),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(85)
        );
        doReturn(expectedResponse).when(this.currencyConverterService).calculateExchange(from, to, amount);

        // when
        var actualResponse = exchangeController.exchange(from, to, amount);

        // then
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(this.currencyConverterService).calculateExchange(from, to, amount);
    }
}