package com.springcloud.calculation.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import com.springcloud.calculation.facade.CurrencyExchangeProxy;
import com.springcloud.model.CalculatedAmount;

@WebMvcTest(CalculationController.class)
public class CalculationControllerTest {

	@MockBean
	private CurrencyExchangeProxy currencyExchangeProxy;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private CalculationController calculationController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCalculateAmount_withRestTemplate() {

		// Arrange
		String from = "USD";
		String to = "INR";
		Long quantity = 100L;

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		CalculatedAmount mockCalculatedAmount = new CalculatedAmount(1L, from, to, 75L, quantity, 7500L, 8080);
		ResponseEntity<CalculatedAmount> responseEntity = new ResponseEntity<>(mockCalculatedAmount, HttpStatus.OK);

		when(restTemplate.getForEntity(anyString(), (CalculatedAmount.class), (uriVariables)))
				.thenReturn(responseEntity);

		// Act
		CalculatedAmount result = calculationController.calculateAmount(from, to, quantity);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(75, result.getConversionMultiple());
		assertEquals(7500, result.getTotalCalculatedAmount());
		verify(restTemplate, times(1)).getForEntity(anyString(), (CalculatedAmount.class), (uriVariables));

	}

	@Test
	void testCalculateAmountFeign() {
		// Arrange
		String from = "USD";
		String to = "INR";
		Long quantity = 100L;

		CalculatedAmount mockCalculatedAmountFeign = new CalculatedAmount(1L, from, to, 75L, quantity, 7500L, 8080);

		when(currencyExchangeProxy.retrieveExchangeValue(from, to)).thenReturn(mockCalculatedAmountFeign);

		// Act
		CalculatedAmount result = calculationController.calculateAmount(from, to, quantity);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(75, result.getConversionMultiple());
		assertEquals(7500, result.getTotalCalculatedAmount());
		verify(currencyExchangeProxy.retrieveExchangeValue(from, to));
	}

	@Test
	void testCalculateAmountServer_withRestTemplate() {
		// Arrange
		String from = "USD";
		String to = "INR";
		Long quantity = 100L;

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		CalculatedAmount mockCalculatedAmount = new CalculatedAmount(1L, from, to, 75L, quantity, 7500L, 8080);

		when(restTemplate.getForObject(anyString(), CalculatedAmount.class, uriVariables))
				.thenReturn(mockCalculatedAmount);

		// Act
		CalculatedAmount result = calculationController.calculateAmount(from, to, quantity);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals(75, result.getConversionMultiple());
		assertEquals(7500, result.getTotalCalculatedAmount());
		verify(restTemplate, times(1)).getForObject(anyString(), CalculatedAmount.class, uriVariables);

	}

}
