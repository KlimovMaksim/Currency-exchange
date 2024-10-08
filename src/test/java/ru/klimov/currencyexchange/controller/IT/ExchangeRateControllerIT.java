package ru.klimov.currencyexchange.controller.IT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/currency_exchange/tests_data.sql")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ExchangeRateControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getExchangeRate_ReturnsExchangeRate() throws Exception {
        // given
        var requestBuilder = get("/exchangeRate/USDGBP");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        content().json("""
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
                            }
                        """),
                        jsonPath("$.exchangeRateId").exists(),
                        jsonPath("$['baseCurrency'].currencyId").exists(),
                        jsonPath("$['targetCurrency'].currencyId").exists());
    }

    @Test
    void getExchangeRate_NoCodePair_BadRequest() throws Exception {
        // given
        var requestBuilder = get("/exchangeRate/");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                        {
                            "detail": "Currency codes must not be empty"
                        }
                        """, false));
    }

    @Test
    void getExchangeRate_ExchangeRateNotFound_NotFound() throws Exception {
        // given
        var requestBuilder = get("/exchangeRate/GBPJPY");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isNotFound(),
                        content().json("""
                        {
                            "detail": "Exchange rate with GBP->JPY not found"
                        }
                        """, false));
    }

    @Test
    void updateExchangeRate_UpdatedExchangeRate() throws Exception {
        // given
        var requestBuilder = patch("/exchangeRate/USDGBP")
                .contentType("application/x-www-form-urlencoded")
                .param("rate", "0.9");

        // when
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().json("""
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
                            "rate": 0.9
                        }
                        """));
    }

    @Test
    void updateExchangeRate_NoRate_BadRequest() throws Exception {
        // given
        var requestBuilder = patch("/exchangeRate/USDGBP").contentType("application/x-www-form-urlencoded");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                        {
                            "detail":"Rate is required and cannot be empty"
                        }
                        """, false));
    }

    @Test
    void updateExchangeRate_ExchangeRateNotFound_NotFound() throws Exception {
        // given
        var requestBuilder = patch("/exchangeRate/JPYAUD")
                .contentType("application/x-www-form-urlencoded")
                .param("rate", "0.9");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isNotFound(),
                        content().json("""
                        {
                            "detail":"Exchange rate with JPY->AUD not found"
                        }
                        """, false));
    }
}