package ru.klimov.currencyexchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.exceptions.ExchangeRateNotFoundException;
import ru.klimov.currencyexchange.exceptions.InvalidExchangeRateDataException;
import ru.klimov.currencyexchange.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    void getExchangeRateByCodePair_ValidCodePair_ReturnsExchangeRate() {
        // given
        String codePair = "USDEUR";
        ExchangeRate expectedExchangeRate = new ExchangeRate();
        expectedExchangeRate.setBaseCurrency(new Currency(1L, "USD", "United States Dollar", "$"));
        expectedExchangeRate.setTargetCurrency(new Currency(2L, "EUR", "Euro", "€"));
        expectedExchangeRate.setRate(new BigDecimal("0.85"));

        doReturn(Optional.of(expectedExchangeRate)).when(exchangeRateRepository)
                .findExchangeRateByCodePair("USD", "EUR");

        // when
        ExchangeRate actualExchangeRate = exchangeRateService.getExchangeRateByCodePair(codePair);

        // then
        assertNotNull(actualExchangeRate);
        assertEquals(expectedExchangeRate, actualExchangeRate);
        verify(exchangeRateRepository, times(1)).findExchangeRateByCodePair("USD", "EUR");
    }

    @Test
    void getExchangeRateByCodePair_CodePairIsNull_ThrowsInvalidExchangeRateDataException() {
        // given
        String codePair = null;

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.getExchangeRateByCodePair(codePair));

        // then
        assertEquals("Currency codes must not be empty", exception.getMessage());
        verify(exchangeRateRepository, never()).findExchangeRateByCodePair(any(), any());
    }

    @Test
    void getExchangeRateByCodePair_CodePairIsEmpty_ThrowsInvalidExchangeRateDataException() {
        // given
        String codePair = "";

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.getExchangeRateByCodePair(codePair));

        // then
        assertEquals("Currency codes must not be empty", exception.getMessage());
        verify(exchangeRateRepository, never()).findExchangeRateByCodePair(any(), any());
    }

    @Test
    void getExchangeRateByCodePair_CodePairIsInvalidLength_ThrowsInvalidExchangeRateDataException() {
        // given
        String codePair = "USDE";

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.getExchangeRateByCodePair(codePair));

        // then
        assertEquals("Currency base and target codes must contain 3 characters each", exception.getMessage());
        verify(exchangeRateRepository, never()).findExchangeRateByCodePair(any(), any());
    }

    @Test
    void getExchangeRateByCodePair_ExchangeRateNotFound_ThrowsExchangeRateNotFoundException() {
        // given
        String codePair = "USDEUR";
        doReturn(Optional.empty()).when(exchangeRateRepository)
                .findExchangeRateByCodePair("USD", "EUR");

        // when
        Exception exception = assertThrows(ExchangeRateNotFoundException.class,
                () -> exchangeRateService.getExchangeRateByCodePair(codePair));

        // then
        assertEquals("Exchange rate with USD->EUR not found", exception.getMessage());
        verify(exchangeRateRepository, times(1)).findExchangeRateByCodePair("USD", "EUR");
    }

    @Test
    void getExchangeRateByBaseAndTarget_ValidBaseAndTarget_ReturnsExchangeRate() {
        // given
        String base = "USD";
        String target = "EUR";
        String codePair = base + target;

        ExchangeRate expectedExchangeRate = new ExchangeRate();
        expectedExchangeRate.setBaseCurrency(new Currency(1L, "USD", "United States Dollar", "$"));
        expectedExchangeRate.setTargetCurrency(new Currency(2L, "EUR", "Euro", "€"));
        expectedExchangeRate.setRate(new BigDecimal("0.85"));

        doReturn(Optional.of(expectedExchangeRate)).when(exchangeRateRepository).findExchangeRateByCodePair(base, target);

        // when
        ExchangeRate actualExchangeRate = exchangeRateService.getExchangeRateByCodePair(base, target);

        // then
        assertNotNull(actualExchangeRate);
        assertEquals(expectedExchangeRate, actualExchangeRate);
        verify(exchangeRateRepository, times(1)).findExchangeRateByCodePair(base, target);
    }

    @Test
    void getExchangeRateByBaseAndTarget_BaseOrTargetIsNull_ThrowsInvalidExchangeRateDataException() {
        // given
        String base = null;
        String target = "EUR";

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.getExchangeRateByCodePair(base, target));

        // then
        assertEquals("Currency codes must not be empty", exception.getMessage());
        verify(exchangeRateRepository, never()).findExchangeRateByCodePair(anyString(), anyString());
    }

    @Test
    void getExchangeRateByBaseAndTarget_BaseOrTargetIsEmpty_ThrowsInvalidExchangeRateDataException() {
        // given
        String base = "";
        String target = "EUR";

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.getExchangeRateByCodePair(base, target));

        // then
        assertEquals("Currency codes must not be empty", exception.getMessage());
        verify(exchangeRateRepository, never()).findExchangeRateByCodePair(anyString(), anyString());
    }

    @Test
    void getExchangeRateByBaseAndTarget_ExchangeRateNotFound_ThrowsExchangeRateNotFoundException() {
        // given
        String base = "USD";
        String target = "EUR";
        String codePair = base + target;

        doReturn(Optional.empty()).when(exchangeRateRepository).findExchangeRateByCodePair(base,target);

        // when
        Exception exception = assertThrows(ExchangeRateNotFoundException.class,
                () -> exchangeRateService.getExchangeRateByCodePair(base, target));

        // then
        assertEquals("Exchange rate with USD->EUR not found", exception.getMessage());
        verify(exchangeRateRepository, times(1)).findExchangeRateByCodePair(base, target);
    }
}