package com.springcloud.calculation.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.springcloud.calculation.facade.CurrencyExchangeProxy;
import com.springcloud.model.CalculatedAmount;

@Service
@Transactional
public class CalculationService {

	Logger logger = LoggerFactory.getLogger(CalculationService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CurrencyExchangeProxy proxy;

	public CalculationService(RestTemplate restTemplate, CurrencyExchangeProxy proxy) {
		this.restTemplate = restTemplate;
		this.proxy = proxy;
	}

	public CalculatedAmount CalculateAmountEntity(Map<String, String> uriVariables, Long quantity) {

		ResponseEntity<CalculatedAmount> responseEntity = CalculateAmountEntityApi(uriVariables);
		CalculatedAmount calculatedAmount = responseEntity.getBody();
		logger.info("Calculated {} from Exchange Service:", calculatedAmount);
		CalculatedAmount amount = new CalculatedAmount(calculatedAmount.getId(), calculatedAmount.getFrom(),
				calculatedAmount.getTo(), calculatedAmount.getConversionMultiple(), quantity,
				quantity * calculatedAmount.getConversionMultiple(), calculatedAmount.getPort());
		logger.info("Calculated from ResponseEntity{} :", amount);
		return amount;
	}

	private ResponseEntity<CalculatedAmount> CalculateAmountEntityApi(Map<String, String> uriVariables) {
		ResponseEntity<CalculatedAmount> responseEntity = restTemplate.getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CalculatedAmount.class, uriVariables);
		return responseEntity;
	}

	public CalculatedAmount CalculateAmountFeign(String from, String to, Long quantity) {

		CalculatedAmount calculatedAmount = proxy.retrieveExchangeValue(from, to);
		logger.info("Calculated {} :", calculatedAmount);
		CalculatedAmount amount = new CalculatedAmount(calculatedAmount.getId(), calculatedAmount.getFrom(),
				calculatedAmount.getTo(), calculatedAmount.getConversionMultiple(), quantity,
				quantity * calculatedAmount.getConversionMultiple(), calculatedAmount.getPort());
		logger.info("Calculated from Feign {} :", amount);
		return amount;
	}

	public CalculatedAmount CalculateAmountServer(Map<String, String> uriVariables, Long quantity) {

		// Load balanced RestTemplate will resolve the service name to a list of servers
		// and balance across them
		CalculatedAmount calculatedAmount = CalculateAmountServerApi(uriVariables);
		CalculatedAmount amount = new CalculatedAmount(calculatedAmount.getId(), calculatedAmount.getFrom(),
				calculatedAmount.getTo(), calculatedAmount.getConversionMultiple(), quantity,
				(quantity * calculatedAmount.getConversionMultiple()), calculatedAmount.getPort());
		logger.info("Calculated from Server {} :", amount);
		return amount;
	}

	private CalculatedAmount CalculateAmountServerApi(Map<String, String> uriVariables) {
		String url = "http://localhost:8000/currency-exchange/from/{from}/to/{to}";
		CalculatedAmount responseEntity = restTemplate.getForObject(url, CalculatedAmount.class, uriVariables);
		return responseEntity;
	}

}
