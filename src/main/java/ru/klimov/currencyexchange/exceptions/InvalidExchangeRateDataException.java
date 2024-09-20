package ru.klimov.currencyexchange.exceptions;

public class InvalidExchangeRateDataException extends RuntimeException {
    public InvalidExchangeRateDataException() {
    }

    public InvalidExchangeRateDataException(String message) {
        super(message);
    }

    public InvalidExchangeRateDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidExchangeRateDataException(Throwable cause) {
        super(cause);
    }

    public InvalidExchangeRateDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
