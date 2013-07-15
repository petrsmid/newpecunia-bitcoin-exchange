package com.petrsmid.bitexchange.bitstamp.impl.dto;

import java.math.BigDecimal;

public class EurUsdRateDTO {
	private BigDecimal buy;
	private BigDecimal sell;
	
	//{"sell": "1.3020", "buy": "1.3126"}
	
	public BigDecimal getBuy() {
		return buy;
	}
	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}
	public BigDecimal getSell() {
		return sell;
	}
	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}
	
	
}
