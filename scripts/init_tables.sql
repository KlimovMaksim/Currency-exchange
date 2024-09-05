create table if not exists Currencies (
    ID integer primary key not null ,
    Code varchar(3) unique ,
    FullName varchar(30) not null ,
    Sing varchar(1) not null
);

create table if not exists ExchangeRates (
    ID integer primary key not null,
    BaseCurrencyId int not null,
    TargetCurrencyId int not null,
    Rate decimal(6),
    unique(BaseCurrencyId, TargetCurrencyId)
);

alter table ExchangeRates add foreign key (BaseCurrencyId) references Currencies(ID);
alter table ExchangeRates add foreign key (TargetCurrencyId) references Currencies(ID);

