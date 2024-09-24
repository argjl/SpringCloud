package com.springcloud.currency.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.springcloud.currency.controller.CurrencyExchangeController;
import com.springcloud.currency.model.ExchangeValue;
import com.springcloud.currency.repository.ExchangeValueRepository;

@Service
public class CurrencyExchangeService {

	Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

	@Autowired
	private Environment environment;

	@Autowired
	private ExchangeValueRepository repository;

	public ExchangeValue CurrencyExchangeValue(String from, String to) {

		ExchangeValue exchangeValue = repository.findByFromAndTo(from, to);
		if (exchangeValue == null) {
			logger.warn("No exchange value found for {} to {}", from, to);
			throw new RuntimeException("Unable to find data for " + from + " to " + to);
		}
		logger.info("exchange value: {}", exchangeValue);
		logger.info(exchangeValue.getFrom());
		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		return exchangeValue;
	}

}
