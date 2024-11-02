package com.currency.exch.discount.calc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.currency.exch.discount.calc.client.ExchangeRateClient;
import com.currency.exch.discount.calc.model.BillDetails;
import com.currency.exch.discount.calc.model.GreetingMessage;
import com.currency.exch.discount.calc.model.User;

public class ExchangeRateServiceTest {

	@Mock
    private ExchangeRateClient exchangeRateClient;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testCalculatePayableAmount_validDiscountForEmployee() {
        BillDetails billDetails = createBillDetails(1, 200, "INR", "USD", "electronics", 1234567891);
        User user = createUser(true, false, LocalDate.now().minusYears(5));
        mockExchangeRateClientResponse(1.0, 80.0);
        when(userService.getUserByIdAndPhone(billDetails.getId(), billDetails.getPhone())).thenReturn(Optional.of(user));

        GreetingMessage result = exchangeRateService.calculatePayableAmount(billDetails);

        assertEquals("Congrats for 30% discount Your final payble amount in USD is: ", result.getMsg());
        assertEquals(140.0, result.getDiscount());
    }
    
    @Test
    void testCalculatePayableAmount_validDiscountForAffiliate() {
        BillDetails billDetails = createBillDetails(2, 200, "INR", "USD", "electronics", 1234567892);
        User user = createUser(false, true, LocalDate.now().minusYears(5));
        mockExchangeRateClientResponse(1.0, 80.0);
        when(userService.getUserByIdAndPhone(billDetails.getId(), billDetails.getPhone())).thenReturn(Optional.of(user));

        GreetingMessage result = exchangeRateService.calculatePayableAmount(billDetails);

        assertEquals("Congrats for 10% discount Your final payble amount in USD is: ", result.getMsg());
        assertEquals(180.0, result.getDiscount());
    }
    
    @Test
    void testCalculatePayableAmount_discountBasedOnYears() {
        BillDetails billDetails = createBillDetails(3, 200, "INR", "USD", "electronics", 1234567893);
        User user = createUser(false, false, LocalDate.now().minusYears(3));
        mockExchangeRateClientResponse(1.0, 80.0);
        when(userService.getUserByIdAndPhone(billDetails.getId(), billDetails.getPhone())).thenReturn(Optional.of(user));

        GreetingMessage result = exchangeRateService.calculatePayableAmount(billDetails);

        assertEquals("Congrats for 05% discount Your final payble amount in USD is: ", result.getMsg());
        assertEquals(190.0, result.getDiscount());
    }

    @Test
    void testCalculatePayableAmount_groceriesCategoryNoDiscount() {
        BillDetails billDetails = createBillDetails(4, 200, "INR", "USD", "groceries", 1234567894);
        User user = createUser(false, false, LocalDate.now().minusYears(1));
        mockExchangeRateClientResponse(1.0, 80.0);
        when(userService.getUserByIdAndPhone(billDetails.getId(), billDetails.getPhone())).thenReturn(Optional.of(user));

        GreetingMessage result = exchangeRateService.calculatePayableAmount(billDetails);

        assertEquals("Sorry You are not eligible for discount Your final payble amount in USD is: ", result.getMsg());
        assertEquals(200.0, result.getDiscount());
    }

    @Test
    void testCalculatePayableAmount_invalidExchangeRates() {
        BillDetails billDetails = createBillDetails(5, 200, "INR", "USD", "electronics", 1234567895);
        when(exchangeRateClient.getExchangeRate(billDetails.getOriginalCurrency())).thenReturn(new HashMap<>());

        Exception exception = assertThrows(RuntimeException.class, () ->
                exchangeRateService.calculatePayableAmount(billDetails));
        assertEquals("Failed to fetch exchange rates.", exception.getMessage());
    }

    @Test
    void testCalculatePayableAmount_invalidConversionRatesFormat() {
        BillDetails billDetails = createBillDetails(6, 200, "INR", "USD", "electronics", 1234567896);
        Map<String, Object> response = new HashMap<>();
        response.put("conversion_rates", "invalid_format");
        when(exchangeRateClient.getExchangeRate(billDetails.getOriginalCurrency())).thenReturn(response);

        Exception exception = assertThrows(RuntimeException.class, () ->
                exchangeRateService.calculatePayableAmount(billDetails));
        assertEquals("Conversion rates are not in expected format.", exception.getMessage());
    }
    
	private BillDetails createBillDetails(int id, double totalAmount, String originalCurrency, String targetCurrency,
			String category, long phone) {
		BillDetails billDetails = BillDetails.builder().id(id).totalAmount(totalAmount).originalCurrency(originalCurrency)
				.targetCurrency(targetCurrency).category(category).phone(phone).build();
		return billDetails;
	}
	
	private User createUser(boolean isEmployee, boolean isAffiliate, LocalDate createdOn) {
        User user = User.builder().employee(isEmployee).affiliate(isAffiliate).createdOn(createdOn).build();
        return user;
    }
	
	private void mockExchangeRateClientResponse(double usdRate, double inrRate) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Double> conversionRates = new HashMap<>();
        conversionRates.put("USD", usdRate);
        conversionRates.put("INR", inrRate);
        response.put("conversion_rates", conversionRates);
        when(exchangeRateClient.getExchangeRate(anyString())).thenReturn(response);
    }
}
