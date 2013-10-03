package com.newpecunia.bitcoind.service;

import java.math.BigDecimal;

public interface ReceiveBTCCallback {
	void onReceiveBTC(BigDecimal amount, String fromAddress, String destinationAddress);
}
