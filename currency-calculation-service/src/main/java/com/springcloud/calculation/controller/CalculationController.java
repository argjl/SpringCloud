package com.springcloud.calculation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springcloud.calculation.service.CalculationService;
import com.springcloud.model.CalculatedAmount;

@RestController
public class CalculationController {

	@Autowired
	private CalculationService calculationService;

	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CalculatedAmount calculateAmount(@PathVariable String from, @PathVariable String to,
			@PathVariable Long quantity) {

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		uriVariables.put("quantity", quantity.toString());
		CalculatedAmount calculateAmount = calculationService.CalculateAmountEntity(uriVariables, quantity);
		return calculateAmount;

	}

	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CalculatedAmount calculateAmountFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable Long quantity) {

		CalculatedAmount calculateAmountFeign = calculationService.CalculateAmountFeign(from, to, quantity);
		return calculateAmountFeign;

	}

	@GetMapping("/currency-converter-server/from/{from}/to/{to}/quantity/{quantity}")
	public CalculatedAmount calculateAmountServer(@PathVariable String from, @PathVariable String to,
			@PathVariable Long quantity) {

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		uriVariables.put("quantity", quantity.toString());
		CalculatedAmount calculateAmount = calculationService.CalculateAmountServer(uriVariables, quantity);
		return calculateAmount;

	}

}
