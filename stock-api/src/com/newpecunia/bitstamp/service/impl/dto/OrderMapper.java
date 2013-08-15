package com.newpecunia.bitstamp.service.impl.dto;

import org.joda.time.DateTime;

import com.newpecunia.bitstamp.service.Order;
import com.newpecunia.bitstamp.service.Order.OrderType;

public class OrderMapper {
	
	public static Order mapOrderDTO2Order(OrderDTO orderDTO) {
		Order order = new Order();
		order.setAmount(orderDTO.getAmount());
		//e.g.: 2013-06-27 15:41:41.177225  or 2013-06-27 15:41:41
		if (orderDTO.getDatetime() != null) {
			String isoDate = orderDTO.getDatetime().replace(' ', 'T');
			order.setDatetime(DateTime.parse(isoDate));
		}
		order.setId(orderDTO.getId());
		order.setPrice(orderDTO.getPrice());
		if (Integer.valueOf(0).equals(orderDTO.getType())) {
			order.setOrderType(OrderType.BUY);
		} else 	if (Integer.valueOf(1).equals(orderDTO.getType())) {
			order.setOrderType(OrderType.SELL);
		}
		return order;
	}
}
