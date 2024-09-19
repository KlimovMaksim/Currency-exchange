package ru.klimov.currencyexchange.exceptions;

public class ExchangeRateAlreadyExistsException extends RuntimeException {
    public ExchangeRateAlreadyExistsException() {
    }

    public ExchangeRateAlreadyExistsException(String message) {
        super(message);
    }

    public ExchangeRateAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeRateAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ExchangeRateAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
