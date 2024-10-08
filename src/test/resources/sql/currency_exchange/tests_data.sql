insert into currency_exchange_db.Currencies (code, fullname, sign) values
    ('USD', 'United States Dollar', '$'),
    ('EUR', 'Euro', '€'),
    ('GBP', 'British Pound', '£'),
    ('JPY', 'Japanese Yen', '¥'),
    ('AUD', 'Australian Dollar', 'A$');

insert into currency_exchange_db.ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) values
    (
        (select id from currency_exchange_db.Currencies where code = 'USD'),
        (select id from currency_exchange_db.Currencies where code = 'EUR'),
        0.85
    ),  -- USD -> EUR

    (
        (select id from currency_exchange_db.Currencies where code = 'USD'),
        (select id from currency_exchange_db.Currencies where code = 'GBP'),
        0.75
    ),  -- USD -> GBP

    (
        (select id from currency_exchange_db.Currencies where code = 'USD'),
        (select id from currency_exchange_db.Currencies where code = 'JPY'),
        110.15
    ), -- USD -> JPY

    (
        (select id from currency_exchange_db.Currencies where code = 'EUR'),
        (select id from currency_exchange_db.Currencies where code = 'USD'),
        1.18
    ),  -- EUR -> USD

    (
        (select id from currency_exchange_db.Currencies where code = 'GBP'),
        (select id from currency_exchange_db.Currencies where code = 'USD'),
        1.33
    ),  -- GBP -> USD

    (
        (select id from currency_exchange_db.Currencies where code = 'JPY'),
        (select id from currency_exchange_db.Currencies where code = 'USD'),
        0.0091
    ), -- JPY -> USD

    (
        (select id from currency_exchange_db.Currencies where code = 'AUD'),
        (select id from currency_exchange_db.Currencies where code = 'USD'),
        0.73
    );  -- AUD -> USD