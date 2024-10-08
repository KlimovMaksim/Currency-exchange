create schema if not exists currency_exchange_db;

create table currency_exchange_db.Currencies (
    ID serial primary key,
    Code varchar(3) unique not null ,
    FullName varchar(30) not null ,
    Sign varchar(2) not null
);

create table currency_exchange_db.ExchangeRates (
     ID serial primary key ,
     BaseCurrencyId int not null references currency_exchange_db.Currencies (ID),
     TargetCurrencyId int not null references currency_exchange_db.Currencies (ID),
     Rate numeric(12, 6) not null,
     unique(BaseCurrencyId, TargetCurrencyId)
);