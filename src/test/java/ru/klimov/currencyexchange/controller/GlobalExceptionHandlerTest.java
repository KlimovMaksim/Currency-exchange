package ru.klimov.currencyexchange.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import ru.klimov.currencyexchange.exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleCurrencyAlreadyExistException() {
        // given
        String errorMessage = "Currency already exist";
        CurrencyAlreadyExistException exception = new CurrencyAlreadyExistException(errorMessage);

        // when
        var actual = globalExceptionHandler.handleCurrencyAlreadyExistException(exception);

        // then
        assertNotNull(actual);
        assertEquals(HttpStatus.CONFLICT, actual.getStatusCode());
        assertEquals(errorMessage, actual.getBody().getDetail());
    }

    @Test
    void handleCurrencyNotFoundException() {
        // given
        String errorMessage = "Currency not found";
        CurrencyNotFoundException exception = new CurrencyNotFoundException(errorMessage);

        // when
        var actual = globalExceptionHandler.handleCurrencyNotFoundException(exception);

        // then
        assertNotNull(actual);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        assertEquals(errorMessage, actual.getBody().getDetail());
    }

    @Test
    void handleInvalidCurrencyDataException() {
        // given
        String errorMessage = "Currency data is invalid";
        InvalidCurrencyDataException exception = new InvalidCurrencyDataException(errorMessage);

        // when
        var actual = globalExceptionHandler.handleInvalidCurrencyDataException(exception);

        // then
        assertNotNull(actual);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals(errorMessage, actual.getBody().getDetail());
    }

    @Test
    void handleExchangeRateAlreadyExistsException() {
        // given
        String errorMessage = "Exchange rate already exist";
        ExchangeRateAlreadyExistsException exception = new ExchangeRateAlreadyExistsException(errorMessage);

        // when
        var actual = globalExceptionHandler.handleExchangeRateAlreadyExistsException(exception);

        // then
        assertNotNull(actual);
        assertEquals(HttpStatus.CONFLICT, actual.getStatusCode());
        assertEquals(errorMessage, actual.getBody().getDetail());
    }

    @Test
    void handleExchangeRateNotFoundException() {
        // given
        String errorMessage = "Exchange rate not found";
        ExchangeRateNotFoundException exception = new ExchangeRateNotFoundException(errorMessage);

        // when
        var actual = globalExceptionHandler.handleExchangeRateNotFoundException(exception);

        // then
        assertNotNull(actual);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        assertEquals(errorMessage, actual.getBody().getDetail());
    }

    @Test
    void handleInvalidExchangeRateDataException() {
        // given
        String errorMessage = "Exchange rate data is invalid";
        InvalidExchangeRateDataException exception = new InvalidExchangeRateDataException(errorMessage);

        // when
        var actual = globalExceptionHandler.handleInvalidExchangeRateDataException(exception);

        // then
        assertNotNull(actual);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertEquals(errorMessage, actual.getBody().getDetail());
    }

    @Test
    void handleCannotGetJdbcConnectionException() {
        // given
        String errorMessage = "Database connection error";
        CannotGetJdbcConnectionException exception = new CannotGetJdbcConnectionException(errorMessage);

        // when
        var actual = globalExceptionHandler.handleCannotGetJdbcConnectionException(exception);

        // then
        assertNotNull(actual);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        assertEquals(errorMessage, actual.getBody().getDetail());
    }

    @Test
    void handleGenericException() {
        // given
        String errorMessage = "An unexpected error occurred";
        Exception exception = new Exception(errorMessage);

        // when
        var actual = globalExceptionHandler.handleGenericException(exception);

        // then
        assertNotNull(actual);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        assertEquals(errorMessage, actual.getBody().getDetail());
    }
}