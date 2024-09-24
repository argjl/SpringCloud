package com.springcloud.currency.controllerTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.springcloud.currency.controller.CurrencyExchangeController;
import com.springcloud.currency.model.ExchangeValue;
import com.springcloud.currency.service.CurrencyExchangeService;

@WebMvcTest(CurrencyExchangeController.class)
public class CurrencyExchangeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CurrencyExchangeService currencyService;

	@InjectMocks
	private CurrencyExchangeController currencyController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(currencyController).build();
	}

	@Test
	public void testRetrieveExchangeValue() throws Exception {
		String from = "USD";
		String to = "INR";

		ExchangeValue mockExchangeValue = new ExchangeValue(10001L, from, to, 70L);
		when(currencyService.CurrencyExchangeValue(from, to)).thenReturn(mockExchangeValue);

		// Act & Assert
		mockMvc.perform(get("/currency-exchange/from/{from}/to/{to}", from, to).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.from").value("USD"))
				.andExpect(jsonPath("$.to").value("INR")).andExpect(jsonPath("$.conversionMultiple").value(70))
				.andDo(print());

		verify(currencyService, times(1)).CurrencyExchangeValue(from, to);
	}

}
