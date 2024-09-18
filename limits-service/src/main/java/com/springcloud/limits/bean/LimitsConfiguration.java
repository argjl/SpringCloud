package com.springcloud.limits.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component
@ConfigurationProperties("limits-service")
@AllArgsConstructor
@Data
public class LimitsConfiguration {
	private LimitsConfiguration() {
	}

	public int maximum;
	public int minimum;

}
