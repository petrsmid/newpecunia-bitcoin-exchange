package com.newpecunia.thymeleaf;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.newpecunia.thymeleaf.controllers.BuyController;
import com.newpecunia.thymeleaf.controllers.SellController;
import com.newpecunia.thymeleaf.controllers.ThymeleafController;

@WebServlet(name = "thymeleaf", urlPatterns = {"/buy/", "/sell/"})
public class ThymeleafServlet extends AbstractThymeleafServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected Map<String, ThymeleafController> getUrl2ControllerMapping() {
		Map<String, ThymeleafController> url2ControllerMapping = new HashMap<>();
		
		url2ControllerMapping.put("/buy/", new BuyController());
		url2ControllerMapping.put("/sell/", new SellController());
		
		return url2ControllerMapping;
	}
	
}