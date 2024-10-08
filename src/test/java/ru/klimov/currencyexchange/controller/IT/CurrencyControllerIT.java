package ru.klimov.currencyexchange.controller.IT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Sql(value = "/sql/currency_exchange/tests_data.sql")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CurrencyControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCurrency_ReturnsCurrency() throws Exception {
        // given
        var requestBuilder = get("/currency/USD");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                        {
                            "code": "USD",
                            "fullName": "United States Dollar",
                            "sign": "$"
                        }
                        """),
                        jsonPath("$.currencyId").exists());
    }

    @Test
    void getCurrency_NoCurrencyCode_BadRequest() throws Exception {
        // given
        var requestBuilder = get("/currency/");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                        {
                            "detail": "Currency code is required and cannot be empty"
                        }
                        """, false));
    }

    @Test
    void getCurrency_CurrencyNotFound_NotFound() throws Exception {
        // given
        var requestBuilder = get("/currency/RUB");

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isNotFound(),
                        content().json("""
                        {
                            "detail": "Currency with code RUB not found"
                        }
                        """, false));
    }
}