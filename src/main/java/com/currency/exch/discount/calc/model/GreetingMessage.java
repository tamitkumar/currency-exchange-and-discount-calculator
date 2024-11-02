package com.currency.exch.discount.calc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GreetingMessage {
	
	private String msg;
	private double discount;
}
