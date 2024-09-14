package ru.klimov.currencyexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    private Long exchangeRateId;

    private Currency baseCurrency;

    private Currency targetCurrency;

    private BigDecimal rate;
}
