package com.springcloud.calculation.controllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.springcloud.calculation.controller.CalculationController;
import com.springcloud.calculation.service.CalculationService;
import com.springcloud.model.CalculatedAmount;

@WebMvcTest(CalculationController.class)
public class CalculationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CalculationService calculationService;

	@InjectMocks
	private CalculatedAmount calculatedAmount;

	@BeforeEach
	void SetUp() {
		calculatedAmount = new CalculatedAmount();
		calculatedAmount.setTotalCalculatedAmount(100L);
		calculatedAmount.setFrom("USD");
		calculatedAmount.setTo("INR");
		calculatedAmount.setQuantity(10L);
		calculatedAmount.setConversionMultiple(10L);
	}

	@Test
	public void testCalculateAmount() throws Exception {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", "USD");
		uriVariables.put("to", "INR");
		uriVariables.put("quantity", "10");

		when(calculationService.CalculateAmountEntity(uriVariables, 10L)).thenReturn(calculatedAmount);

		mockMvc.perform(get("/currency-converter/from/USD/to/INR/quantity/10")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalCalculatedAmount").value(100L)).andExpect(jsonPath("$.from").value("USD"))
				.andExpect(jsonPath("$.to").value("INR")).andExpect(jsonPath("$.quantity").value(10));
	}

	@Test
	public void testCalculateAmountFeign() throws Exception {
		when(calculationService.CalculateAmountFeign("USD", "INR", 10L)).thenReturn(calculatedAmount);

		mockMvc.perform(get("/currency-converter-feign/from/USD/to/INR/quantity/10")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalCalculatedAmount").value(100.0)).andExpect(jsonPath("$.from").value("USD"))
				.andExpect(jsonPath("$.to").value("INR")).andExpect(jsonPath("$.quantity").value(10));
	}

	@Test
	public void testCalculateAmountServer() throws Exception {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", "USD");
		uriVariables.put("to", "INR");
		uriVariables.put("quantity", "10");

		when(calculationService.CalculateAmountServer(uriVariables, 10L)).thenReturn(calculatedAmount);

		mockMvc.perform(get("/currency-converter-server/from/USD/to/INR/quantity/10")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalCalculatedAmount").value(100.0)).andExpect(jsonPath("$.from").value("USD"))
				.andExpect(jsonPath("$.to").value("INR")).andExpect(jsonPath("$.quantity").value(10));
	}

}
