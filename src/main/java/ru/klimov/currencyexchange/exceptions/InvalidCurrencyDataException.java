package ru.klimov.currencyexchange.exceptions;

public class InvalidCurrencyDataException extends RuntimeException {
    public InvalidCurrencyDataException() {
    }

    public InvalidCurrencyDataException(String message) {
        super(message);
    }

    public InvalidCurrencyDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCurrencyDataException(Throwable cause) {
        super(cause);
    }

    public InvalidCurrencyDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
