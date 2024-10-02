insert into currency_exchange_db.Currencies (code, fullname, sign) values
   ('USD', 'United States Dollar', '$'),
   ('EUR', 'Euro', '€'),
   ('GBP', 'British Pound', '£'),
   ('JPY', 'Japanese Yen', '¥'),
   ('AUD', 'Australian Dollar', 'A$');

insert into currency_exchange_db.ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) values
    (1, 2, 0.85),  -- USD -> EUR
    (1, 3, 0.75),  -- USD -> GBP
    (1, 4, 110.15), -- USD -> JPY
    (2, 1, 1.18),  -- EUR -> USD
    (3, 1, 1.33),  -- GBP -> USD
    (4, 1, 0.0091), -- JPY -> USD
    (5, 1, 0.73);  -- AUD -> USD