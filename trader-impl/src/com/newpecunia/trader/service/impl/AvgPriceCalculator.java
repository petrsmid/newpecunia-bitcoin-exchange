package com.newpecunia.trader.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.newpecunia.bitstamp.service.PriceAndAmount;

public class AvgPriceCalculator {

	private static final Logger logger = LogManager.getLogger(AvgPriceCalculator.class);	
	
	public static BigDecimal calculateAvgPrice(List<PriceAndAmount> orders, BigDecimal forMaxBtcs) {
		BigDecimal sumAmount = BigDecimal.ZERO;
		BigDecimal sumPrice = BigDecimal.ZERO;
		int i = 0;
		for (PriceAndAmount order : orders) {
			logger.debug("Considering order number "+ ++i +" for price");
			sumAmount = sumAmount.add(order.getAmount());
			sumPrice = sumPrice.add(order.getPrice().multiply(order.getAmount()));
			if (sumAmount.compareTo(forMaxBtcs) >= 0) {
				break;
			}
		}
		BigDecimal avgPrice = sumPrice.divide(sumAmount, 2, RoundingMode.HALF_UP);
		return avgPrice;
	}	
}
