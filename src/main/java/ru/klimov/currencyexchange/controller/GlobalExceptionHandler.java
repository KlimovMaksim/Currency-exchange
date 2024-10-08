package ru.klimov.currencyexchange.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.klimov.currencyexchange.exceptions.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ProblemDetail> createProblemDetail(HttpStatus status, String detail) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detail));
    }

    @ExceptionHandler(CurrencyAlreadyExistException.class)
    public ResponseEntity<ProblemDetail> handleCurrencyAlreadyExistException(CurrencyAlreadyExistException ex) {
        return createProblemDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCurrencyNotFoundException(CurrencyNotFoundException ex) {
        return createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidCurrencyDataException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCurrencyDataException(InvalidCurrencyDataException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ExchangeRateAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleExchangeRateAlreadyExistsException(ExchangeRateAlreadyExistsException ex) {
        return createProblemDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ExchangeRateNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleExchangeRateNotFoundException(ExchangeRateNotFoundException ex) {
        return createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidExchangeRateDataException.class)
    public ResponseEntity<ProblemDetail> handleInvalidExchangeRateDataException(InvalidExchangeRateDataException ex) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ResponseEntity<ProblemDetail> handleCannotGetJdbcConnectionException(CannotGetJdbcConnectionException ex) {
        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Database connection error");
    }
}
