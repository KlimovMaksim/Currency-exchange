package ru.klimov.currencyexchange.controller.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.klimov.currencyexchange.controller.CurrencyController;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.service.CurrencyService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CurrencyController currencyController;

    @Test
    void getCurrency_ReturnsValidCurrency() {
        // given
        String currencyCode = "USD";
        Currency expectedCurrency = new Currency(1L, "USD", "United State Dollar", "$");
        doReturn(expectedCurrency).when(this.currencyService).getCurrency(currencyCode);

        // when
        var actualCurrency = this.currencyController.getCurrency(currencyCode);

        // then
        assertNotNull(actualCurrency);
        assertEquals(HttpStatus.OK, actualCurrency.getStatusCode());
        assertEquals(expectedCurrency, actualCurrency.getBody());
        verify(this.currencyService).getCurrency(currencyCode);
    }
}