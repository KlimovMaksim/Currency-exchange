package ru.klimov.currencyexchange.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.klimov.currencyexchange.entity.Currency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCurrencyRepository implements CurrencyRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCurrencyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Currency> findByCurrencyCode(String currencyCode) {
        List<Currency> result = jdbcTemplate.query("select id, code, fullname, sign from Currencies where code=?",
                this::mapRowToCurrency,
                currencyCode);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    private Currency mapRowToCurrency(ResultSet resultSet, int rowNum) throws SQLException {
        return new Currency(
                (long) resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("fullname"),
                resultSet.getString("sign"));
    }

    @Override
    public Iterable<Currency> findAll() {
        return jdbcTemplate.query("select id, code, fullname, sign from Currencies",
                this::mapRowToCurrency);
    }

    @Override
    @Transactional
    public Currency save(Currency entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "insert into Currencies (code, fullname, sign) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, entity.getCode());
            ps.setString(2, entity.getFullName());
            ps.setString(3, entity.getSign());
            return ps;
        }, keyHolder);

        if (keyHolder.getKeys().get("id") != null) {
            Object generatedId = keyHolder.getKeys().get("id");
            if (generatedId instanceof Number) {
                entity.setCurrencyId(((Number) generatedId).longValue());
            } else {
                throw new IllegalStateException("Generated ID is not a valid number");
            }
        }

        return entity;
    }
}
