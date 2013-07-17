package com.newpecunia.bitstamp.service.impl.dto;

import com.newpecunia.bitstamp.service.AccountBalance;

public class AccountBalanceMapper {
	
	public static AccountBalance mapAccountBalanceDTO2AccountBalance(AccountBalanceDTO dto) {
		AccountBalance ab = new AccountBalance();
		ab.setBtcAvailable(dto.getBtc_available());
		ab.setBtcBalance(dto.getBtc_balance());
		ab.setBtcReserved(dto.getBtc_reserved());
		ab.setCustomerTradingFee(dto.getFee());
		ab.setUsdAvailable(dto.getUsd_available());
		ab.setUsdBalance(dto.getUsd_balance());
		ab.setUsdReserved(dto.getUsd_reserved());
		
		return ab;
	}

}
