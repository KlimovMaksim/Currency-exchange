package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.*;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.exceptions.CurrencyAlreadyExistException;
import ru.klimov.currencyexchange.exceptions.CurrencyNotFoundException;
import ru.klimov.currencyexchange.exceptions.InvalidCurrencyDataException;
import ru.klimov.currencyexchange.service.CurrencyService;

import java.util.Map;

@RestController
@RequestMapping(path = "/currencies", produces = "application/json")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public Iterable<Currency> getCurrencies() {
        return currencyService.findAllCurrencies();
    }

    @GetMapping("/{currencyCode}")
    public Currency getCurrency(@PathVariable String currencyCode) {
        return currencyService.getCurrency(currencyCode);
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.CREATED)
    public Currency createCurrency(@RequestParam Map<String, String> currencyParamMap) {
        return currencyService.createCurrency(currencyParamMap);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCurrencyNotFoundException(CurrencyNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(InvalidCurrencyDataException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCurrencyDataException(InvalidCurrencyDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(CurrencyAlreadyExistException.class)
    public ResponseEntity<ProblemDetail> handleCurrencyAlreadyExistException(CurrencyAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    public ResponseEntity<ProblemDetail> handleCannotGetJdbcConnectionException(CannotGetJdbcConnectionException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Database connection error"));
    }
}
