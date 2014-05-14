package com.newpecunia.trader.service.impl.processor;

import java.math.BigDecimal;

public interface BitstampWithdrawOrderManager {

	void orderWithdrawBtcFromBitstampToWallet(BigDecimal amount);

	void withdrawOfBtcFullfilled(BigDecimal amount);

	BigDecimal getBtcAmountToWithdraw();

}
