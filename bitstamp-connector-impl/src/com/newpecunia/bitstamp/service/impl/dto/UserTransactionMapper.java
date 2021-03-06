package com.newpecunia.bitstamp.service.impl.dto;

import org.joda.time.DateTime;

import com.newpecunia.bitstamp.service.UserTransaction;

public class UserTransactionMapper {
	
	public static UserTransaction mapUserTransactionDTO2UserTransaction(UserTransactionDTO dto) {
		UserTransaction ut = new UserTransaction();
		ut.setAmountBTC(dto.getBtc());
		ut.setAmountUSD(dto.getUsd());
		ut.setBtcPrice(dto.getBtc_usd());
		//e.g.: 2013-06-27 15:41:41.177225  or 2013-06-27 15:41:41
		if (dto.getDatetime() != null) {
			String isoDate = dto.getDatetime().replace(' ', 'T');
			ut.setDatetime(DateTime.parse(isoDate));
		}
		ut.setOrderId(dto.getOrder_id());
		ut.setTransactionFee(dto.getFee());
		ut.setTransactionId(dto.getId());
		if (Integer.valueOf(0).equals(dto.getType())) {
			ut.setType(UserTransaction.TransactionType.DEPOSIT);
		} else if (Integer.valueOf(1).equals(dto.getType())) {
			ut.setType(UserTransaction.TransactionType.WITHDRAWAL);
		} else if (Integer.valueOf(2).equals(dto.getType())) {
			ut.setType(UserTransaction.TransactionType.TRADE);
		}
		
		return ut;
	}

}
