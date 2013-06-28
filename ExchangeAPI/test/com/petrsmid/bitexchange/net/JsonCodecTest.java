package com.petrsmid.bitexchange.net;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import com.petrsmid.bitexchange.bitstamp.impl.dto.OrderDTO;

public class JsonCodecTest {

	private String jsonOrder = "{\"price\": \"10000\", \"amount\": \"0.01\", \"type\": 1, \"id\": 4477442, \"datetime\": \"2013-06-27 15:41:41.177225\"}";
	private String jsonOrderList = "[{\"price\": \"10000.00\", \"amount\": \"0.01000000\", \"type\": 1, \"id\": 4477442, \"datetime\": \"2013-06-27 15:41:41\"}," +
									 "{\"price\": \"10000.00\", \"amount\": \"0.01000000\", \"type\": 1, \"id\": 4477442, \"datetime\": \"2013-06-27 15:41:41\"}]";
	
	@Test
	public void parseFromJson() throws JsonParsingException {
		OrderDTO order = JsonCodec.INSTANCE.parseJson(jsonOrder, OrderDTO.class);
		assertEquals(new BigDecimal("10000"), order.getPrice());
		assertEquals(new BigDecimal("0.01"), order.getAmount());
		assertEquals("4477442", order.getId());
		assertEquals("2013-06-27 15:41:41.177225", order.getDatetime());
	}
	
	@Test
	public void parseListFromJson() throws JsonParsingException {
		OrderDTO[] orders = JsonCodec.INSTANCE.parseJson(jsonOrderList, OrderDTO[].class);
		assertEquals(2, orders.length);
	}
}
