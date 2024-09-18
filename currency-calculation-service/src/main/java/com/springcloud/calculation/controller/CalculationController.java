package com.springcloud.calculation.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springcloud.calculation.facade.CurrencyExchangeProxy;
import com.springcloud.model.CalculatedAmount;

@RestController
public class CalculationController {

	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CalculatedAmount calculateAmount(@PathVariable String from, @PathVariable String to,
			@PathVariable Long quantity) {

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CalculatedAmount> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CalculatedAmount.class, uriVariables);
		CalculatedAmount calculatedAmount = responseEntity.getBody();
		return new CalculatedAmount(calculatedAmount.getId(), calculatedAmount.getFrom(), calculatedAmount.getTo(),
				calculatedAmount.getConversionMultiple(), quantity, quantity * calculatedAmount.getConversionMultiple(),
				calculatedAmount.getPort());
//		System.out.println(calculatedAmount);
//		return calculatedAmount;
	}

	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CalculatedAmount calculateAmountFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable Long quantity) {

		CalculatedAmount calculatedAmount = proxy.retrieveExchangeValue(from, to);

		return new CalculatedAmount(calculatedAmount.getId(), calculatedAmount.getFrom(), calculatedAmount.getTo(),
				calculatedAmount.getConversionMultiple(), quantity, quantity * calculatedAmount.getConversionMultiple(),
				calculatedAmount.getPort());
//		System.out.println(calculatedAmount);
//		return calculatedAmount;
	}
	
	@GetMapping("/currency-converter-server/from/{from}/to/{to}/quantity/{quantity}")
    public CalculatedAmount calculateAmountServer(@PathVariable String from, @PathVariable String to, @PathVariable Long quantity) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        // Load balanced RestTemplate will resolve the service name to a list of servers and balance across them
        String url = "http://currency-exchange-service/currency-exchange/from/{from}/to/{to}";
        CalculatedAmount calculatedAmount = restTemplate.getForObject(url, CalculatedAmount.class, uriVariables);

        return new CalculatedAmount(
                calculatedAmount.getId(),
                calculatedAmount.getFrom(),
                calculatedAmount.getTo(),
                calculatedAmount.getConversionMultiple(),
                quantity,
                quantity * calculatedAmount.getConversionMultiple(),
                calculatedAmount.getPort()
        );
    }

}
