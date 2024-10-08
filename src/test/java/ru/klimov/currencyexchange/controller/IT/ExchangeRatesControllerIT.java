package ru.klimov.currencyexchange.controller.IT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/currency_exchange/tests_data.sql")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ExchangeRatesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getExchangeRates_AllExchangeRates() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/exchangeRates");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                                [
                                {
                                    "baseCurrency": {
                                        "code": "USD",
                                        "fullName": "United States Dollar",
                                        "sign": "$"
                                    },
                                    "targetCurrency": {
                                        "code": "EUR",
                                        "fullName": "Euro",
                                        "sign": "€"
                                    },
                                    "rate": 0.85
                                },
                                {
                                    "baseCurrency": {
                                        "code": "USD",
                                        "fullName": "United States Dollar",
                                        "sign": "$"
                                    },
                                    "targetCurrency": {
                                        "code": "GBP",
                                        "fullName": "British Pound",
                                        "sign": "£"
                                    },
                                    "rate": 0.75
                                },
                                {
                                    "baseCurrency": {
                                        "code": "USD",
                                        "fullName": "United States Dollar",
                                        "sign": "$"
                                    },
                                    "targetCurrency": {
                                        "code": "JPY",
                                        "fullName": "Japanese Yen",
                                        "sign": "¥"
                                    },
                                    "rate": 110.15
                                },
                                {
                                    "baseCurrency": {
                                        "code": "EUR",
                                        "fullName": "Euro",
                                        "sign": "€"
                                    },
                                    "targetCurrency": {
                                        "code": "USD",
                                        "fullName": "United States Dollar",
                                        "sign": "$"
                                    },
                                    "rate": 1.18
                                },
                                {
                                    "baseCurrency": {
                                        "code": "GBP",
                                        "fullName": "British Pound",
                                        "sign": "£"
                                    },
                                    "targetCurrency": {
                                        "code": "USD",
                                        "fullName": "United States Dollar",
                                        "sign": "$"
                                    },
                                    "rate": 1.33
                                },
                                {
                                    "baseCurrency": {
                                        "code": "JPY",
                                        "fullName": "Japanese Yen",
                                        "sign": "¥"
                                    },
                                    "targetCurrency": {
                                        "code": "USD",
                                        "fullName": "United States Dollar",
                                        "sign": "$"
                                    },
                                    "rate": 0.0091
                                },
                                {
                                    "baseCurrency": {
                                        "code": "AUD",
                                        "fullName": "Australian Dollar",
                                        "sign": "A$"
                                    },
                                    "targetCurrency": {
                                        "code": "USD",
                                        "fullName": "United States Dollar",
                                        "sign": "$"
                                    },
                                    "rate": 0.73
                                }
                                ]
                                """, false),
                        jsonPath("$[*].exchangeRateId").exists(),
                        jsonPath("$[*].baseCurrency.currencyId").exists(),
                        jsonPath("$[*].targetCurrency.currencyId").exists());
    }

    @Test
    void createExchangeRate_CreateExchangeRate() throws Exception {
        // given
        var requestBuilder = post("/exchangeRates")
                .contentType("application/x-www-form-urlencoded")
                .param("baseCurrencyCode", "JPY")
                .param("targetCurrencyCode", "GBP")
                .param("rate", "0.0052");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isCreated(),
                        content().json("""
                                {
                                    "baseCurrency": {
                                        "code": "JPY",
                                        "fullName": "Japanese Yen",
                                        "sign": "¥"
                                    },
                                    "targetCurrency": {
                                        "code": "GBP",
                                        "fullName": "British Pound",
                                        "sign": "£"
                                    },
                                    "rate": 0.0052
                                }
                                """),
                        jsonPath("$.exchangeRateId").exists(),
                        jsonPath("$['baseCurrency'].currencyId").exists(),
                        jsonPath("$['targetCurrency'].currencyId").exists()
                );
    }

    @Test
    void createExchangeRate_NoTargetCurrency_BadRequest() throws Exception {
        // give
        var requestBuilder = post("/exchangeRates")
                .contentType("application/x-www-form-urlencoded")
                .param("baseCurrencyCode", "JPY")
                .param("rate", "0.0052");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                                {
                                    "detail": "Currency codes must not be empty"
                                }
                                """, false)
                );
    }

    @Test
    void createExchangeRate_ExchangeRateAlreadyExists_Conflict() throws Exception {
        // given
        var requestBuilder = post("/exchangeRates")
                .contentType("application/x-www-form-urlencoded")
                .param("baseCurrencyCode", "JPY")
                .param("targetCurrencyCode", "USD")
                .param("rate", "0.0091");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isConflict(),
                        content().json("""
                                {
                                    "detail": "Exchange rate JPY->USD already exists"
                                }
                                """, false)
                );
    }

    @Test
    void createExchangeRate_CurrencyNotFound_NotFound() throws Exception {
        // given
        var requestBuilder = post("/exchangeRates")
                .contentType("application/x-www-form-urlencoded")
                .param("baseCurrencyCode", "RUB")
                .param("targetCurrencyCode", "USD")
                .param("rate", "0.0118");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isNotFound(),
                        content().json("""
                                {
                                    "detail": "Currency with code RUB not found"
                                }
                                """, false)
                );

    }
}