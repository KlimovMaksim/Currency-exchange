create table if not exists Currencies (
    ID integer primary key generated by default as identity,
    Code varchar(3) unique not null ,
    FullName varchar(30) not null ,
    Sign varchar(2) not null
);

create table if not exists ExchangeRates (
    ID integer primary key generated by default as identity,
    BaseCurrencyId int not null,
    TargetCurrencyId int not null,
    Rate numeric(12, 6) not null,
    unique(BaseCurrencyId, TargetCurrencyId)
);

alter table ExchangeRates add foreign key (BaseCurrencyId) references Currencies(ID);
alter table ExchangeRates add foreign key (TargetCurrencyId) references Currencies(ID);

insert into Currencies (code, fullname, sign) values
    ('USD', 'United States Dollar', '$'),
    ('EUR', 'Euro', '€'),
    ('GBP', 'British Pound', '£'),
    ('JPY', 'Japanese Yen', '¥'),
    ('AUD', 'Australian Dollar', 'A$');

insert into ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) values
    (1, 2, 0.85),  -- USD -> EUR
    (1, 3, 0.75),  -- USD -> GBP
    (1, 4, 110.15), -- USD -> JPY
    (2, 1, 1.18),  -- EUR -> USD
    (3, 1, 1.33),  -- GBP -> USD
    (4, 1, 0.0091), -- JPY -> USD
    (5, 1, 0.73);  -- AUD -> USD