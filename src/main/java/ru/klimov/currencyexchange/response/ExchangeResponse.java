package ru.klimov.currencyexchange.response;

import lombok.Data;
import ru.klimov.currencyexchange.entity.Currency;

import java.math.BigDecimal;

@Data
public class ExchangeResponse {

    private Currency baseCurrency;

    private Currency targetCurrency;

    private BigDecimal rate;

    private BigDecimal amount;

    private BigDecimal convertedAmount;
}
