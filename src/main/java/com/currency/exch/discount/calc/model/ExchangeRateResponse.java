package com.currency.exch.discount.calc.model;

import lombok.Data;

@Data
public class ExchangeRateResponse {
	private String baseCode;
    private String targetCode;
    private double conversionRate;
}
