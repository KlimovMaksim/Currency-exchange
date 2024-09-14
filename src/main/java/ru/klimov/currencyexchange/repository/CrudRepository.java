package ru.klimov.currencyexchange.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {

    Iterable<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    void delete(ID id);

    void update(T entity);
}
