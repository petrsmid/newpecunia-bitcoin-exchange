package com.newpecunia.bitcoind.service;

import java.math.BigDecimal;

public interface ReceiveMoneyCallback {
	void onReceiveMoney(BigDecimal amount, String fromAddress, String destinationAddress);
}
