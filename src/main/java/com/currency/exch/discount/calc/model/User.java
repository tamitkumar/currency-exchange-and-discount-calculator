package com.currency.exch.discount.calc.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
	private int id;
    private String name;
    private String email;
    private long phone;
    private Boolean employee;
    private Boolean affiliate;
    private LocalDate createdOn;
    private LocalDate modifiedOn;
}
