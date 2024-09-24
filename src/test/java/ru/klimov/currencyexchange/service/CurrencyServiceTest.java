package ru.klimov.currencyexchange.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.exceptions.CurrencyAlreadyExistException;
import ru.klimov.currencyexchange.exceptions.CurrencyNotFoundException;
import ru.klimov.currencyexchange.exceptions.InvalidCurrencyDataException;
import ru.klimov.currencyexchange.repository.CurrencyRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void findAllCurrencies_ReturnsAllCurrencies() {
        // given
        List<Currency> expected = List.of(new Currency(3L, "RUB", "Ruble", "₽"),
                new Currency(4L, "USD", "United States Dollar", "$"),
                new Currency(5L, "GBP", "British Pound", "£"));
        doReturn(expected).when(this.currencyRepository).findAll();

        // when
        var actual = this.currencyService.findAllCurrencies();

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(this.currencyRepository, times(1)).findAll();
    }

    @Test
    void getCurrency_CurrencyCodeIsValid_ReturnsCurrency() {
        // given
        String currencyCode = "USD";
        Currency expectedCurrency = new Currency(4L, currencyCode, "United States Dollar", "$");
        doReturn(Optional.of(expectedCurrency)).when(this.currencyRepository).findByCurrencyCode(currencyCode);

        // when
        var actual = this.currencyService.getCurrency(currencyCode);

        // then
        assertNotNull(actual);
        assertEquals(expectedCurrency, actual);
        verify(this.currencyRepository, times(1)).findByCurrencyCode(currencyCode);
    }

    @Test
    void getCurrency_CurrencyCodeIsEmpty_InvalidCurrencyDataException() {
        // given
        String currencyCode = "";

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.getCurrency(currencyCode));
        assertEquals("Currency code is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).findByCurrencyCode(any());
    }

    @Test
    void getCurrency_CurrencyCodeIsNull_InvalidCurrencyDataException() {
        // given
        String currencyCode = null;

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.getCurrency(currencyCode));
        assertEquals("Currency code is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).findByCurrencyCode(any());
    }

    @Test
    void getCurrency_CurrencyCodeNotFound_CurrencyNotFoundException() {
        // given
        String currencyCode = "USD";
        doReturn(Optional.empty()).when(this.currencyRepository).findByCurrencyCode(currencyCode);

        // when
        // then
        Exception actualException = assertThrows(CurrencyNotFoundException.class,
                () -> this.currencyService.getCurrency(currencyCode));
        assertEquals("Currency with code " + currencyCode + " not found", actualException.getMessage());
        verify(this.currencyRepository, times(1)).findByCurrencyCode(currencyCode);
    }

    @Test
    void createCurrency_CurrencyCodeIsValid_ReturnsCurrency() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("fullname", "United States Dollar");
        currencyParamMap.put("sign", "$");
        Currency expectedCurrency = new Currency(4L, "USD", "United States Dollar", "$");
        doReturn(expectedCurrency).when(this.modelMapper).map(currencyParamMap, Currency.class);
        doReturn(expectedCurrency).when(this.currencyRepository).save(expectedCurrency);

        // when
        var actual = this.currencyService.createCurrency(currencyParamMap);

        // then
        assertNotNull(actual);
        assertNotNull(actual.getCurrencyId());
        assertEquals(expectedCurrency.getCode(), actual.getCode());
        assertEquals(expectedCurrency.getFullName(), actual.getFullName());
        assertEquals(expectedCurrency.getSign(), actual.getSign());
        verify(this.modelMapper, times(1)).map(currencyParamMap, Currency.class);
        verify(this.currencyRepository, times(1)).save(expectedCurrency);
        verifyNoMoreInteractions(this.currencyRepository);
    }

    @Test
    void createCurrency_CurrencyCodeIsEmpty_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "");
        currencyParamMap.put("fullname", "United States Dollar");
        currencyParamMap.put("sign", "$");

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency code is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_CurrencyFullnameIsEmpty_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("fullname", "");
        currencyParamMap.put("sign", "$");

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency fullname is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_CurrencySignIsEmpty_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("fullname", "United States Dollar");
        currencyParamMap.put("sign", "");

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency sign is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_ThereIsNoCurrencyCode_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("fullname", "United States Dollar");
        currencyParamMap.put("sign", "$");

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency code is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_ThereIsNoCurrencyFullname_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("sign", "$");

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency fullname is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_ThereIsNoCurrencySign_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("fullname", "United States Dollar");

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency sign is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_CurrencyCodeIsNull_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", null);
        currencyParamMap.put("fullname", "United States Dollar");
        currencyParamMap.put("sign", "$");

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency code is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_CurrencyFullnameIsNull_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("fullname", null);
        currencyParamMap.put("sign", "$");

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency fullname is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_CurrencySignIsNull_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("fullname", "United States Dollar");
        currencyParamMap.put("sign", null);

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency sign is required and cannot be empty", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_DuplicateCurrencyCode_CurrencyAlreadyExistException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();
        currencyParamMap.put("code", "USD");
        currencyParamMap.put("fullname", "United States Dollar");
        currencyParamMap.put("sign", "$");
        Currency expectedCurrency = new Currency(4L, "USD", "United States Dollar", "$");
        doReturn(expectedCurrency).when(this.modelMapper).map(currencyParamMap, Currency.class);
        when(this.currencyRepository.save(expectedCurrency)).thenThrow(DuplicateKeyException.class);

        // when
        // then
        Exception actualException = assertThrows(CurrencyAlreadyExistException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Currency with code " + currencyParamMap.get("code") + " already exists",
                actualException.getMessage());
        verifyNoMoreInteractions(this.currencyRepository);
    }

    @Test
    void createCurrency_CurrencyParamMapIsNull_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = null;

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Parameters code, fullname, sign are required", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }

    @Test
    void createCurrency_CurrencyParamMapIsEmpty_InvalidCurrencyDataException() {
        // given
        Map<String, String> currencyParamMap = new LinkedHashMap<>();

        // when
        // then
        Exception actualException = assertThrows(InvalidCurrencyDataException.class,
                () -> this.currencyService.createCurrency(currencyParamMap));
        assertEquals("Parameters code, fullname, sign are required", actualException.getMessage());
        verify(this.currencyRepository, never()).save(any());
    }
}