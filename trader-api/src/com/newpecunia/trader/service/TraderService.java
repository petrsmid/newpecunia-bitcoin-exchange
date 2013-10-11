package com.newpecunia.trader.service;

import java.math.BigDecimal;


public interface TraderService {
	
	BigDecimal getNPBtcBuyPriceInUSD(BigDecimal amountBtc);
	BigDecimal getNPBtcSellPriceInUSD(BigDecimal amountBtc);

}
