package ru.klimov.currencyexchange.repository;

import ru.klimov.currencyexchange.entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findExchangeRateByCodePair(String base, String target);
}
