package com.currency.exch.discount.calc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.currency.exch.discount.calc.model.BillDetails;
import com.currency.exch.discount.calc.model.GreetingMessage;
import com.currency.exch.discount.calc.services.ExchangeRateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/exchange")
@Tag(name = "Currency Exchange", description = "Operations related to currency exchange")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
	
	public ExchangeRateController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	@PostMapping("/exchange-calc")
	@Operation(summary = "Calculate payable amount in target currency", description = "Calculates the amount in target currency based on the exchange rate")
    public ResponseEntity<String> getExchangeRate(@Parameter(description = "Details of the bill including currency, amount, etc.") @RequestBody BillDetails billDetails) {
		GreetingMessage msg = exchangeRateService.calculatePayableAmount(billDetails);
		return ResponseEntity.ok(msg.getMsg() + msg.getDiscount());
    }
}
