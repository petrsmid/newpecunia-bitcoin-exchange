package com.petrsmid.bitexchange.bitstamp.impl.dto;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

import com.petrsmid.bitexchange.bitstamp.Order;

public class OrderMapperTest {
	
	@Test
	public void testNullOrderMapper() {
		OrderDTO dto = new OrderDTO();
		Order order = OrderMapper.mapOrderDTO2Order(dto);
		assertNull(order.getAmount());
		assertNull(order.getPrice());
		assertNull(order.getDatetime());
		assertNull(order.getId());
		assertNull(order.getOrderType());
	}
	
	//2013-06-27 15:41:42.177225  or 2013-06-27 15:41:41

	@Test
	public void testSmallerDate() {
		OrderDTO dto = new OrderDTO();
		dto.setDatetime("2013-06-27 15:41:42");
		Order order = OrderMapper.mapOrderDTO2Order(dto);
		DateTime expectedDate = DateTime.parse("2013-06-27T15:41:42");
		assertEquals(expectedDate, order.getDatetime());
	}

	@Test
	public void testLongerDate() {
		OrderDTO dto = new OrderDTO();
		dto.setDatetime("2013-06-27 15:41:42.177225");
		Order order = OrderMapper.mapOrderDTO2Order(dto);
		DateTime expectedDate = DateTime.parse("2013-06-27T15:41:42.177225");
		assertEquals(2013, order.getDatetime().getYear());
		assertEquals(6, order.getDatetime().getMonthOfYear());
		assertEquals(27, order.getDatetime().getDayOfMonth());
		assertEquals(15, order.getDatetime().getHourOfDay());
		assertEquals(41, order.getDatetime().getMinuteOfHour());
		assertEquals(42, order.getDatetime().getSecondOfMinute());
		assertEquals(177, order.getDatetime().getMillisOfSecond());
		assertEquals(expectedDate, order.getDatetime());
	}	
	
}
