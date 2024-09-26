package ru.klimov.currencyexchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    void findAllExchangeRates_ReturnsAllExchangeRates() {
        // given
        List<ExchangeRate> expected = List.of(
                new ExchangeRate(2L,
                        new Currency(4L, "USD", "United States Dollar", "$"),
                        new Currency(5L, "GBP", "British Pound", "£"),
                        BigDecimal.valueOf(3.15)),
                new ExchangeRate(3L,
                        new Currency(4L, "USD", "United States Dollar", "$"),
                        new Currency(6L, "RUB", "Ruble", "₽"),
                        BigDecimal.valueOf(100.15))
        );
        doReturn(expected).when(exchangeRateRepository).findAll();

        // when
        var actual = exchangeRateService.findAllExchangeRates();

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(this.exchangeRateRepository, times(1)).findAll();
    }

    @Test
    void getExchangeRateByCodePair_CodePairIsValid_ReturnsExchangeRate(){

    }
}