package com.petrsmid.bitexchange.bitstamp;

import java.math.BigDecimal;

public class EurUsdRate {
	
	private BigDecimal buyEurRate;
	private BigDecimal sellEurRate;
	
	
	public EurUsdRate(BigDecimal buyEurRate, BigDecimal sellEurRate) {
		super();
		this.buyEurRate = buyEurRate;
		this.sellEurRate = sellEurRate;
	}

	/**
	 * @return how much USD one has to pay to get 1 EUR
	 */
	public BigDecimal getBuyEurRate() {
		return buyEurRate;
	}

	/**
	 * @return how much USD one gets for 1 EUR
	 */
	public BigDecimal getSellEurRate() {
		return sellEurRate;
	}
	
}
