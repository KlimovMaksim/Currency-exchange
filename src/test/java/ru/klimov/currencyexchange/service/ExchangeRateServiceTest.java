package ru.klimov.currencyexchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;
import ru.klimov.currencyexchange.exceptions.ExchangeRateAlreadyExistsException;
import ru.klimov.currencyexchange.exceptions.ExchangeRateNotFoundException;
import ru.klimov.currencyexchange.exceptions.InvalidExchangeRateDataException;
import ru.klimov.currencyexchange.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Test
    void createExchangeRate_ValidParams_SuccessfulCreation() {
        // given
        Map<String, String> exchangeRateParams = new HashMap<>();
        exchangeRateParams.put("baseCurrencyCode", "USD");
        exchangeRateParams.put("targetCurrencyCode", "EUR");
        exchangeRateParams.put("rate", "0.85");

        Currency baseCurrency = new Currency(1L, "USD", "United States Dollar", "$");
        Currency targetCurrency = new Currency(2L, "EUR", "Euro", "€");
        ExchangeRate expectedExchangeRate = new ExchangeRate();
        expectedExchangeRate.setBaseCurrency(baseCurrency);
        expectedExchangeRate.setTargetCurrency(targetCurrency);
        expectedExchangeRate.setRate(new BigDecimal("0.85"));

        when(currencyService.getCurrency(exchangeRateParams.get("baseCurrencyCode"))).thenReturn(baseCurrency);
        when(currencyService.getCurrency(exchangeRateParams.get("targetCurrencyCode"))).thenReturn(targetCurrency);
        when(exchangeRateRepository.save(expectedExchangeRate)).thenReturn(expectedExchangeRate);

        // when
        ExchangeRate actualExchangeRate = exchangeRateService.createExchangeRate(exchangeRateParams);

        // then
        assertNotNull(actualExchangeRate);
        assertEquals(expectedExchangeRate, actualExchangeRate);
        verify(exchangeRateRepository, times(1)).save(expectedExchangeRate);
    }

    // todo check other
    @Test
    void createExchangeRate_MissingBaseCurrency_ThrowsInvalidExchangeRateDataException() {
        // given
        Map<String, String> exchangeRateParams = new HashMap<>();
        exchangeRateParams.put("targetCurrencyCode", "EUR");
        exchangeRateParams.put("rate", "0.85");

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.createExchangeRate(exchangeRateParams));

        // then
        assertEquals("Currency codes must not be empty", exception.getMessage());
        verify(exchangeRateRepository, never()).save(any(ExchangeRate.class));
    }

    @Test
    void createExchangeRate_EmptyRateParam_ThrowsInvalidExchangeRateDataException() {
        // given
        Map<String, String> exchangeRateParams = new HashMap<>();
        exchangeRateParams.put("baseCurrencyCode", "USD");
        exchangeRateParams.put("targetCurrencyCode", "EUR");
        exchangeRateParams.put("rate", "");

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.createExchangeRate(exchangeRateParams));

        // then
        assertEquals("Rate is required and cannot be empty", exception.getMessage());
        verify(exchangeRateRepository, never()).save(any(ExchangeRate.class));
    }

    @Test
    void createExchangeRate_DuplicateExchangeRate_ThrowsExchangeRateAlreadyExistsException() {
        // given
        Map<String, String> exchangeRateParams = new HashMap<>();
        exchangeRateParams.put("baseCurrencyCode", "USD");
        exchangeRateParams.put("targetCurrencyCode", "EUR");
        exchangeRateParams.put("rate", "0.85");

        Currency baseCurrency = new Currency(1L, "USD", "United States Dollar", "$");
        Currency targetCurrency = new Currency(2L, "EUR", "Euro", "€");

        when(currencyService.getCurrency("USD")).thenReturn(baseCurrency);
        when(currencyService.getCurrency("EUR")).thenReturn(targetCurrency);
        when(exchangeRateRepository.save(any(ExchangeRate.class)))
                .thenThrow(new DuplicateKeyException("Duplicate"));

        // when
        Exception exception = assertThrows(ExchangeRateAlreadyExistsException.class,
                () -> exchangeRateService.createExchangeRate(exchangeRateParams));

        // then
        assertEquals("Exchange rate USD->EUR already exists", exception.getMessage());
        verify(exchangeRateRepository, times(1)).save(any(ExchangeRate.class));
    }

    @Test
    void updateExchangeRate_ValidParams_SuccessfulUpdate() {
        // given
        String codePair = "USDEUR";
        Map<String, String> rateParamMap = new HashMap<>();
        rateParamMap.put("rate", "0.85");

        ExchangeRate existingExchangeRate = new ExchangeRate();
        existingExchangeRate.setBaseCurrency(new Currency(1L, "USD", "United States Dollar", "$"));
        existingExchangeRate.setTargetCurrency(new Currency(2L, "EUR", "Euro", "€"));
        existingExchangeRate.setRate(new BigDecimal("0.80"));

        when(exchangeRateRepository.findExchangeRateByCodePair("USD", "EUR"))
                .thenReturn(Optional.of(existingExchangeRate));

        // when
        ExchangeRate updatedExchangeRate = exchangeRateService.updateExchangeRate(codePair, rateParamMap);

        // then
        assertNotNull(updatedExchangeRate);
        assertEquals(new BigDecimal("0.85"), updatedExchangeRate.getRate());
        verify(exchangeRateRepository, times(1)).update(existingExchangeRate);
    }

    @Test
    void updateExchangeRate_InvalidCodePair_ThrowsInvalidExchangeRateDataException() {
        // given
        String invalidCodePair = "US";
        Map<String, String> rateParamMap = new HashMap<>();
        rateParamMap.put("rate", "0.85");

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.updateExchangeRate(invalidCodePair, rateParamMap));

        // then
        assertEquals("Currency base and target codes must contain 3 characters each", exception.getMessage());
        verify(exchangeRateRepository, never()).update(any(ExchangeRate.class));
    }

    @Test
    void updateExchangeRate_MissingRateParam_ThrowsInvalidExchangeRateDataException() {
        // given
        String codePair = "USDEUR";
        Map<String, String> rateParamMap = new HashMap<>();

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.updateExchangeRate(codePair, rateParamMap));

        // then
        assertEquals("Rate is required and cannot be empty", exception.getMessage());
        verify(exchangeRateRepository, never()).update(any(ExchangeRate.class));
    }

    @Test
    void updateExchangeRate_ExchangeRateNotFound_ThrowsExchangeRateNotFoundException() {
        // given
        String codePair = "USDEUR";
        Map<String, String> rateParamMap = new HashMap<>();
        rateParamMap.put("rate", "0.85");

        when(exchangeRateRepository.findExchangeRateByCodePair("USD", "EUR"))
                .thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(ExchangeRateNotFoundException.class,
                () -> exchangeRateService.updateExchangeRate(codePair, rateParamMap));

        // then
        assertEquals("Exchange rate with USD->EUR not found", exception.getMessage());
        verify(exchangeRateRepository, never()).update(any(ExchangeRate.class));
    }

    @Test
    void updateExchangeRate_NullCodePair_ThrowsInvalidExchangeRateDataException() {
        // given
        String codePair = null;
        Map<String, String> rateParamMap = new HashMap<>();
        rateParamMap.put("rate", "0.85");

        // when
        Exception exception = assertThrows(InvalidExchangeRateDataException.class,
                () -> exchangeRateService.updateExchangeRate(codePair, rateParamMap));

        // then
        assertEquals("Currency codes must not be empty", exception.getMessage());
        verify(exchangeRateRepository, never()).update(any(ExchangeRate.class));
    }
}