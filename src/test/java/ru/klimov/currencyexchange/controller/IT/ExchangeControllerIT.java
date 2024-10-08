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

@Sql("/sql/currency_exchange/tests_data.sql")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class ExchangeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void exchange_ExchangeResponse() throws Exception {
        // given
        var requestBuilder = get("/exchange")
                .param("from", "USD")
                .param("to", "EUR")
                .param("amount", "133");

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
                                 "code": "EUR",
                                 "fullName": "Euro",
                                 "sign": "€"
                             },
                             "rate": 0.85,
                             "amount": 133.00,
                             "convertedAmount": 113.05
                         }
                        """),
                        jsonPath("$['baseCurrency'].currencyId").exists(),
                        jsonPath("$['targetCurrency'].currencyId").exists());
    }
}