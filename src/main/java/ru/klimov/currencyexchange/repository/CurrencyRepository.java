package ru.klimov.currencyexchange.repository;

import ru.klimov.currencyexchange.entity.Currency;

import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    Optional<Currency> findByCurrencyCode(String currencyCode);
}
