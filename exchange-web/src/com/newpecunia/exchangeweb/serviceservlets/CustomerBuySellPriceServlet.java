package com.newpecunia.exchangeweb.serviceservlets;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.net.JsonCodec;
import com.newpecunia.trader.service.TraderService;

@Singleton
public class CustomerBuySellPriceServlet extends AbstractServiceServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private TraderService traderService;
	
	private class Response {
		private BigDecimal buyPrice;
		private BigDecimal sellPrice;

		public Response(BigDecimal buyPrice, BigDecimal sellPrice) {
			this.buyPrice = buyPrice;
			this.sellPrice = sellPrice;
		}
		public BigDecimal getBuyPrice() {
			return buyPrice;
		}
		public BigDecimal getSellPrice() {
			return sellPrice;
		}
	}
	
	@Override
	protected void serveGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		BigDecimal buyPrice = traderService.getCustomerBtcBuyPriceInUSD();
		BigDecimal sellPrice = traderService.getCustomerBtcSellPriceInUSD();
		
		Response response = new Response(buyPrice, sellPrice);
		String strResponse = JsonCodec.INSTANCE.toJson(response);
		resp.getOutputStream().println(strResponse);
		
	};

}
