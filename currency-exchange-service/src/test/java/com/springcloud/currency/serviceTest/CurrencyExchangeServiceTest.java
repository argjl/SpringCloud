package com.springcloud.currency.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import com.springcloud.currency.model.ExchangeValue;
import com.springcloud.currency.repository.ExchangeValueRepository;
import com.springcloud.currency.service.CurrencyExchangeService;

@WebMvcTest(CurrencyExchangeService.class)
public class CurrencyExchangeServiceTest {

	@Mock
	private Environment environment;

	@MockBean
	private ExchangeValueRepository repository;

	@InjectMocks
	private CurrencyExchangeService currencyService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCurrencyExchangeValueSuccess() throws Exception {
		String from = "USD";
		String to = "INR";

		ExchangeValue mockExchangeValue = new ExchangeValue(10001L, from, to, 70L);
		when(repository.findByFromAndTo(from, to)).thenReturn(mockExchangeValue);
		when(environment.getProperty("local.server.port")).thenReturn("8000");

		// When
		ExchangeValue exchangeValue = currencyService.CurrencyExchangeValue(from, to);

		// Then
		assertNotNull(exchangeValue);
		assertEquals(from, exchangeValue.getFrom());
		assertEquals(to, exchangeValue.getTo());
		assertEquals(70, exchangeValue.getConversionMultiple());
		assertEquals(8000, exchangeValue.getPort());

		verify(repository).findByFromAndTo(from, to);
		verify(environment).getProperty("local.server.port");

	}

	@Test
	void testCurrencyExchangeValueNotFound() throws Exception {

		String from = "EUR";
		String to = "INR";

		when(repository.findByFromAndTo(from, to)).thenReturn(null);

		// When & Then
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> currencyService.CurrencyExchangeValue(from, to));

		assertEquals("Unable to find data for EUR to INR", exception.getMessage());

		verify(repository).findByFromAndTo(from, to);
	}

}
