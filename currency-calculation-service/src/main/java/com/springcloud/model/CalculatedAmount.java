package com.springcloud.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class CalculatedAmount {

	@Getter
	@Setter
	private Long id;
	@Getter
	@Setter
	private String from;
	@Getter
	@Setter
	private String to;
	@Getter
	@Setter
	private Long conversionMultiple;
	@Getter
	@Setter
	private Long quantity;
	@Getter
	@Setter
	private Long totalCalculatedAmount;
	@Getter
	@Setter
	private int port;

	public CalculatedAmount(Long id, String from, String to, Long conversionMultiple, Long quantity,
			Long totalCalculatedAmount, int port) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.conversionMultiple = conversionMultiple;
		this.quantity = quantity;
		this.totalCalculatedAmount = totalCalculatedAmount;
		this.port = port;
	}

	@Override
	public String toString() {
		return "CalculatedAmount [id=" + id + ", from=" + from + ", to=" + to + ", conversionMultiple="
				+ conversionMultiple + ", quantity=" + quantity + ", totalCalculatedAmount=" + totalCalculatedAmount
				+ ", port=" + port + "]";
	}

}
