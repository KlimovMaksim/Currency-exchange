package ru.klimov.currencyexchange.repository;

import java.util.Optional;

public interface CrudRepository<T> {

    Iterable<T> findAll();

    T save(T entity);
}
