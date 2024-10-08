package ru.klimov.currencyexchange.controller.IT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
                                "code": "USD",
                                "fullName": "United States Dollar",
                                "sign": "$"
                            },
                            {
                                "code": "EUR",
                                "fullName": "Euro",
                                "sign": "€"
                            },
                            {
                                "code": "GBP",
                                "fullName": "British Pound",
                                "sign": "£"
                            },
                            {
                                "code": "JPY",
                                "fullName": "Japanese Yen",
                                "sign": "¥"
                            },
                            {
                                "code": "AUD",
                                "fullName": "Australian Dollar",
                                "sign": "A$"
                            }
                        ]
                        """),
                        jsonPath("$[*].currencyId").exists());
    }

    @Test
    void createCurrencies_CurrencyAlreadyExists_Conflict() throws Exception {
        // given
        var requestBuilder = post("/currencies")
                .contentType("application/x-www-form-urlencoded")
                .param("code", "USD")
                .param("fullname", "United States Dollar")
                .param("sign", "$");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isConflict(),
                        content().json("""
                        {
                            "detail":"Currency with code USD already exists"
                        }
                        """, false));
    }

    @Test
    void createCurrencies_ReturnsCreatedCurrencies() throws Exception {
        // given
        var requestBuilder = post("/currencies")
                .contentType("application/x-www-form-urlencoded")
                .param("code", "RUB")
                .param("fullname", "Ruble")
                .param("sign", "₽");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isCreated(),
                        content().json("""
                        {
                            "code": "RUB",
                            "fullName": "Ruble",
                            "sign": "₽"
                        }
                        """, false),
                        jsonPath("$.currencyId").exists());
    }

    @Test
    void createCurrencies_NoCurrencyCode_BadRequest() throws Exception {
        // given
        var requestBuilder = post("/currencies")
                .contentType("application/x-www-form-urlencoded")
                .param("fullname", "Ruble")
                .param("sign", "₽");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                        {
                            "detail":"Currency code is required and cannot be empty"
                        }
                        """, false));
    }
}