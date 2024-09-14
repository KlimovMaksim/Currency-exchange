package ru.klimov.currencyexchange.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.entity.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcExchangeRateRepository implements ExchangeRateRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcExchangeRateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ExchangeRate> findExchangeRateByCodePair(String base, String target) {
        String sql = """
                select
                    e.id,
                    bc.id,
                    bc.fullname,
                    bc.code,
                    bc.sign,
                    tc.id,
                    tc.fullname,
                    tc.code,
                    tc.sign,
                    e.rate
                from exchangerates e
                join currencies bc on e.basecurrencyid = bc.id
                join currencies tc on e.targetcurrencyid = tc.id
                where bc.code = ? and tc.code = ?
                """;
        List<ExchangeRate> result = jdbcTemplate.query(sql, this::mapRowToExchangeRate, base, target);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Iterable<ExchangeRate> findAll() {
        String sql = """
                select
                    e.id,
                    bc.id,
                    bc.code,
                    bc.fullname,
                    bc.sign,
                    tc.id,
                    tc.code,
                    tc.fullname,
                    tc.sign,
                    e.rate
                    from exchangerates e
                join currencies bc on e.basecurrencyid = bc.id
                join currencies tc on e.targetcurrencyid = tc.id
                """;
        return jdbcTemplate.query(sql, this::mapRowToExchangeRate);
    }

    private ExchangeRate mapRowToExchangeRate(ResultSet resultSet, int i) throws SQLException {
        return new ExchangeRate((long) resultSet.getInt(1), new Currency((long) resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)), new Currency((long) resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8), resultSet.getString(9)), resultSet.getBigDecimal(10));
    }

    @Override
    public Optional<ExchangeRate> findById(Long id) {
        String sql = """
                select
                    e.id,
                    bc.id,
                    bc.fullname,
                    bc.code,
                    bc.sign,
                    tc.id,
                    tc.fullname,
                    tc.code,
                    tc.sign,
                    e.rate
                    from exchangerates e
                join currencies bc on e.basecurrencyid = bc.id
                join currencies tc on e.targetcurrencyid = tc.id
                where e.id = ?
                """;
        List<ExchangeRate> list = jdbcTemplate.query(sql, this::mapRowToExchangeRate, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) {
        String sql = """
                insert into exchangerates (basecurrencyid, targetcurrencyid, rate)
                values
                (
                 (select id from currencies where code = ?),
                 (select id from currencies where code = ?),
                 1.96
                )
                """;
        jdbcTemplate.update(sql, entity.getBaseCurrency().getCode(), entity.getTargetCurrency().getCode());
        return entity;
    }

    @Override
    public void delete(Long id) {
        // todo
    }

    @Override
    public void update(ExchangeRate entity) {
        // todo
        String sql = """
                update exchangerates
                set rate = ?
                where id = ?
                """;
        jdbcTemplate.update(sql, entity.getRate(), entity.getExchangeRateId());
    }
}
