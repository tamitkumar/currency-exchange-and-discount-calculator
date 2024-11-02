package com.currency.exch.discount.calc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillDetails {
	private int id;
	private double totalAmount;
    private String originalCurrency;
    private String targetCurrency;
    private String category;
    private long phone;
}
