package com.springcloud.calculation.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.springcloud.calculation.facade.CurrencyExchangeProxy;
import com.springcloud.calculation.service.CalculationService;
import com.springcloud.model.CalculatedAmount;

@WebMvcTest(CalculationService.class)
public class CalculationServiceTest {

	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private CurrencyExchangeProxy proxy;

	@InjectMocks
	private CalculationService calculationService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		calculationService = new CalculationService(restTemplate, proxy);
	}

	@Test
	void testCalculateAmountEntity() throws Exception {
		// Arrange
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", "USD");
		uriVariables.put("to", "INR");

		Long quantity = 100L;

		CalculatedAmount mockcalculatedAmount = new CalculatedAmount(10001L, "USD", "INR", 70L, quantity, 7000L, 8000);

		ResponseEntity<CalculatedAmount> responseEntity = ResponseEntity.ok(mockcalculatedAmount);

		when(restTemplate.getForEntity(anyString(), eq(CalculatedAmount.class), eq(uriVariables)))
				.thenReturn(responseEntity);

		// Act

		CalculatedAmount result = calculationService.CalculateAmountEntity(uriVariables, quantity);

		// Assert
		assertEquals(mockcalculatedAmount.getFrom(), result.getFrom());
		assertEquals(mockcalculatedAmount.getTo(), result.getTo());
		assertEquals(mockcalculatedAmount.getConversionMultiple() * quantity, result.getTotalCalculatedAmount());
		verify(restTemplate, times(1)).getForEntity(anyString(), eq(CalculatedAmount.class), eq(uriVariables));

	}

	@Test
	void testCalculateAmountFeign() {
		// Arrange
		String from = "USD";
		String to = "INR";
		Long quantity = 100L;

		CalculatedAmount mockCalculatedAmount = new CalculatedAmount(10001L, "USD", "INR", 70L, 100L, 7000L, 8000);

		when(proxy.retrieveExchangeValue(from, to)).thenReturn(mockCalculatedAmount);

		// Act
		CalculatedAmount result = calculationService.CalculateAmountFeign(from, to, quantity);

		// Assert
		assertEquals(mockCalculatedAmount.getFrom(), result.getFrom());
		assertEquals(mockCalculatedAmount.getTo(), result.getTo());
		assertEquals(mockCalculatedAmount.getConversionMultiple() * quantity, result.getTotalCalculatedAmount());
		verify(proxy, times(1)).retrieveExchangeValue(from, to);
	}

	@Test
	void testCalculateAmountServer() throws Exception {
		// Arrange
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", "USD");
		uriVariables.put("to", "INR");

		Long quantity = 100L;

		CalculatedAmount mockcalculatedAmount = new CalculatedAmount(10001L, "USD", "INR", 70L, quantity, 7000L, 8000);

		when(restTemplate.getForObject(anyString(), eq(CalculatedAmount.class), eq(uriVariables)))
				.thenReturn(mockcalculatedAmount);

		// Act

		CalculatedAmount result = calculationService.CalculateAmountServer(uriVariables, quantity);

		// Assert
		assertEquals(mockcalculatedAmount.getFrom(), result.getFrom());
		assertEquals(mockcalculatedAmount.getTo(), result.getTo());
		assertEquals(mockcalculatedAmount.getConversionMultiple() * quantity, result.getTotalCalculatedAmount());
		verify(restTemplate, times(1)).getForObject(anyString(), eq(CalculatedAmount.class), eq(uriVariables));

	}

	// Testing the private methods using reflection
	@Test
	void testCalculateAmountEntityApiPrivateMethod() throws Exception {
		// Arrange
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", "USD");
		uriVariables.put("to", "INR");

		CalculatedAmount mockCalculatedAmount = new CalculatedAmount(10001L, "USD", "INR", 70L, 100L, 7000L, 8000);
		ResponseEntity<CalculatedAmount> responseEntity = ResponseEntity.ok(mockCalculatedAmount);

		when(restTemplate.getForEntity(anyString(), eq(CalculatedAmount.class), eq(uriVariables)))
				.thenReturn(responseEntity);

		// Use reflection to invoke the private method
		Method method = CalculationService.class.getDeclaredMethod("CalculateAmountEntityApi", Map.class);
		method.setAccessible(true);

		// Act
		@SuppressWarnings("unchecked")
		ResponseEntity<CalculatedAmount> result = (ResponseEntity<CalculatedAmount>) method.invoke(calculationService,
				uriVariables);

		// Assert
		assertEquals(responseEntity.getBody().getFrom(), result.getBody().getFrom());
		assertEquals(responseEntity.getBody().getTo(), result.getBody().getTo());
	}

	@Test
	void testCalculateAmountServerApiPrivateMethod() throws Exception {
		// Arrange
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", "USD");
		uriVariables.put("to", "INR");

		CalculatedAmount mockCalculatedAmount = new CalculatedAmount(10001L, "USD", "INR", 70L, 100L, 7000L, 8000);

		when(restTemplate.getForObject(anyString(), eq(CalculatedAmount.class), eq(uriVariables)))
				.thenReturn(mockCalculatedAmount);

		// Use reflection to invoke the private method
		Method method = CalculationService.class.getDeclaredMethod("CalculateAmountServerApi", Map.class);
		method.setAccessible(true);

		// Act
		CalculatedAmount result = (CalculatedAmount) method.invoke(calculationService, uriVariables);

		// Assert
		assertEquals(mockCalculatedAmount.getFrom(), result.getFrom());
		assertEquals(mockCalculatedAmount.getTo(), result.getTo());
		assertEquals(mockCalculatedAmount.getConversionMultiple(), result.getConversionMultiple());
	}

}
