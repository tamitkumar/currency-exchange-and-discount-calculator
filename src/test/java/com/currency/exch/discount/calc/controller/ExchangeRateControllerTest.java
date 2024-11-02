package com.currency.exch.discount.calc.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.currency.exch.discount.calc.model.BillDetails;
import com.currency.exch.discount.calc.model.GreetingMessage;
import com.currency.exch.discount.calc.services.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testGetExchangeRate() throws Exception {
        BillDetails billDetails = BillDetails.builder().id(1).category("ele").originalCurrency("ALL").targetCurrency("USD").totalAmount(300).phone(1234567891).build();
        GreetingMessage greetingMessage = GreetingMessage.builder().msg("Payable Amount: ").discount(5).build();
        when(exchangeRateService.calculatePayableAmount(billDetails)).thenReturn(greetingMessage);
        mockMvc.perform(post("/api/v1/exchange/exchange-calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(billDetails)))
                .andExpect(status().isOk())
                .andExpect(content().string("Payable Amount: " + 5.0d));
    }
}
