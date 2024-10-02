package ru.klimov.currencyexchange.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("/sql/currency_exchange/tests_data.sql")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CurrenciesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCurrencies_ReturnsAllCurrencies() throws Exception {
        // given
        var requestBuilder = get("/currencies");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                                [
                                    {
                                        "currencyId": 1,
                                        "code": "USD",
                                        "fullName": "United States Dollar",
                                        "sign": "$"
                                    },
                                    {
                                        "currencyId": 2,
                                        "code": "EUR",
                                        "fullName": "Euro",
                                        "sign": "€"
                                    },
                                    {
                                        "currencyId": 3,
                                        "code": "GBP",
                                        "fullName": "British Pound",
                                        "sign": "£"
                                    },
                                    {
                                        "currencyId": 4,
                                        "code": "JPY",
                                        "fullName": "Japanese Yen",
                                        "sign": "¥"
                                    },
                                    {
                                        "currencyId": 5,
                                        "code": "AUD",
                                        "fullName": "Australian Dollar",
                                        "sign": "A$"
                                    }
                                ]
                                """)
                );
    }
}