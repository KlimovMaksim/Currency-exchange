package ru.klimov.currencyexchange.exceptions;

public class CurrencyAlreadyExistException extends RuntimeException{

    public CurrencyAlreadyExistException() {
    }

    public CurrencyAlreadyExistException(String message) {
        super(message);
    }

    public CurrencyAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public CurrencyAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
