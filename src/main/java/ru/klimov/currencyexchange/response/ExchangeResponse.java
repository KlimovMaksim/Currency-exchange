package ru.klimov.currencyexchange.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.klimov.currencyexchange.entity.Currency;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExchangeResponse {

    private Currency baseCurrency;

    private Currency targetCurrency;

    private BigDecimal rate;

    private BigDecimal amount;

    private BigDecimal convertedAmount;
}
