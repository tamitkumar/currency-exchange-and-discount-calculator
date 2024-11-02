package com.currency.exch.discount.calc.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchangeRateClient", url = "${exchangerate.api.url}")
public interface ExchangeRateClient {
	
	@GetMapping("/{base}")
	Map<String, Object> getExchangeRate(@PathVariable("base") String baseCurrency);
}
