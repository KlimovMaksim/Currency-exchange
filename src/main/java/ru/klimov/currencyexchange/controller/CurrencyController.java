package ru.klimov.currencyexchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.klimov.currencyexchange.entity.Currency;
import ru.klimov.currencyexchange.repository.JdbcCurrencyRepository;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/currencies", produces = "application/json")
public class CurrencyController {

    private final JdbcCurrencyRepository currencyRepository;

    @Autowired
    public CurrencyController(JdbcCurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    public Iterable<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Currency> getCurrency(@PathVariable String code){
        Optional<Currency> result = currencyRepository.findByCurrencyCode(code);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.CREATED)
    public Currency createCurrency(@RequestParam Map<String,String> currencyParamMap){
        Currency currency = new Currency();
        currency.setCode(currencyParamMap.get("code"));
        currency.setFullName(currencyParamMap.get("fullname"));
        currency.setSign(currencyParamMap.get("sign"));
        currencyRepository.save(currency);
        return currency;
    }
}
