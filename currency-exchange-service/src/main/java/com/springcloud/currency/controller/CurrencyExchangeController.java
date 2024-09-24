package com.springcloud.currency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springcloud.currency.model.ExchangeValue;
import com.springcloud.currency.service.CurrencyExchangeService;

@RestController
public class CurrencyExchangeController {

	@Autowired
	private CurrencyExchangeService currencyService;

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {

		ExchangeValue exchangeValue = currencyService.CurrencyExchangeValue(from, to);
		return exchangeValue;
	}
}
