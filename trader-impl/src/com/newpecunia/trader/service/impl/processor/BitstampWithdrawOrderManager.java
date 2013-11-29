package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

public interface BitstampWithdrawOrderManager {

	void orderWithdrawUsdToUnicredit(BigDecimal amount);
	
	void orderWithdrawBtcFromBitstampToWallet(BigDecimal amount);

	void withdrawOfUsdFullfilled(BigDecimal amount);

	void withdrawOfBtcFullfilled(BigDecimal amount);

	BigDecimal getUsdAmountToWithdraw();

	BigDecimal getBtcAmountToWithdraw();

}
