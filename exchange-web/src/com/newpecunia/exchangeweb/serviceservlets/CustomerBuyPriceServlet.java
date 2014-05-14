package com.newpecunia.exchangeweb.serviceservlets;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.net.JsonCodec;
import com.newpecunia.trader.service.PriceService;

@Singleton
public class CustomerBuyPriceServlet extends AbstractServiceServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PriceService traderService;
	
	private class Response {
		private BigDecimal buyPrice;

		public Response(BigDecimal buyPrice) {
			this.buyPrice = buyPrice;
		}
		@SuppressWarnings("unused")
		public BigDecimal getBuyPrice() {
			return buyPrice;
		}
	}
	
	@Override
	protected void serveGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		BigDecimal buyPrice = traderService.getCustomerBtcBuyPriceInUSD();
		
		Response response = new Response(buyPrice);
		String strResponse = JsonCodec.INSTANCE.toJson(response);
		resp.getOutputStream().println(strResponse);
		
	};

}
