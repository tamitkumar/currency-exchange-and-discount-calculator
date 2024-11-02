package com.currency.exch.discount.calc.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.currency.exch.discount.calc.client.ExchangeRateClient;
import com.currency.exch.discount.calc.model.BillDetails;
import com.currency.exch.discount.calc.model.GreetingMessage;
import com.currency.exch.discount.calc.model.User;

@Service
public class ExchangeRateService {

	private final ExchangeRateClient exchangeRateClient;

	private final UserService userService;

	public ExchangeRateService(ExchangeRateClient exchangeRateClient, UserService userService) {
		this.exchangeRateClient = exchangeRateClient;
		this.userService = userService;
	}

	@SuppressWarnings("unchecked")
	public GreetingMessage calculatePayableAmount(BillDetails billDetails) {
		Map<String, Object> response = exchangeRateClient.getExchangeRate(billDetails.getOriginalCurrency());
		if (!response.containsKey("conversion_rates")) {
			throw new RuntimeException("Failed to fetch exchange rates.");
		}
		Map<String, Double> rates = new HashMap<>();
		Object conversionRatesObj = response.get("conversion_rates");
		if (conversionRatesObj instanceof Map) {
			Map<String, ?> conversionRates = (Map<String, ?>) conversionRatesObj;
			for (Map.Entry<String, ?> entry : conversionRates.entrySet()) {
				rates.put(entry.getKey(), convertToDouble(entry.getValue()));
			}
		} else {
			throw new RuntimeException("Conversion rates are not in expected format.");
		}
		
		double exchangeRate = rates.getOrDefault(billDetails.getTargetCurrency(), 0.0);
		double conversionRate = rates.getOrDefault("USD", 0.0);
		double amountInUSD = billDetails.getTotalAmount() * conversionRate;
		billDetails.setTotalAmount(amountInUSD);
		Optional<User> user = userService.getUserByIdAndPhone(billDetails.getId(), billDetails.getPhone());
		GreetingMessage greetingMessage = GreetingMessage.builder().build();
		user.ifPresent(existingUser -> calculateDiscount(existingUser, billDetails, greetingMessage));
		double totalAfterDiscount = billDetails.getTotalAmount() - greetingMessage.getDiscount();
		greetingMessage.setMsg(greetingMessage.getMsg()
				.concat("Your final payble amount in " + billDetails.getTargetCurrency() + " is: "));
		greetingMessage.setDiscount(totalAfterDiscount * exchangeRate);
		return greetingMessage;
	}

	private void calculateDiscount(User user, BillDetails billDetails, GreetingMessage greetingMessage) {
		long yearsSinceCreation = ChronoUnit.YEARS.between(user.getCreatedOn(), LocalDate.now());
		if (user.getEmployee()) {
			greetingMessage.setMsg("Congrats for 30% discount ");
			greetingMessage.setDiscount(billDetails.getTotalAmount() * 0.30); // 30% discount
		} else if (user.getAffiliate()) {
			greetingMessage.setMsg("Congrats for 10% discount ");
			greetingMessage.setDiscount(billDetails.getTotalAmount() * 0.10); // 10% discount
		} else if (yearsSinceCreation > 2) {
			greetingMessage.setMsg("Congrats for 05% discount ");
			greetingMessage.setDiscount(billDetails.getTotalAmount() * 0.05); // 5% discount
		} else if (billDetails.getTotalAmount() >= 100) {
			int fullHundreds = (int) (billDetails.getTotalAmount() / 100);
			greetingMessage.setMsg("Congrats You are eligible for 5% discount ");
			greetingMessage.setDiscount(fullHundreds * 5.0);
		}
		if ("groceries".equalsIgnoreCase(billDetails.getCategory()) || greetingMessage.getMsg() == null) {
			greetingMessage.setMsg("Sorry You are not eligible for discount ");
			greetingMessage.setDiscount(0.0);
		}
	}

	private Double convertToDouble(Object value) {
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		throw new IllegalArgumentException("Invalid conversion rate value: " + value);
	}
}
