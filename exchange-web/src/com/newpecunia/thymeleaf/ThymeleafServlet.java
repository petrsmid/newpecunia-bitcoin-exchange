package com.newpecunia.thymeleaf;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.newpecunia.thymeleaf.controllers.BuyController;
import com.newpecunia.thymeleaf.controllers.ThymeleafController;

@Singleton
public class ThymeleafServlet extends AbstractThymeleafServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private BuyController buyController;

	
	@Override
	protected Map<String, ThymeleafController> getUrl2ControllerMapping() {
		Map<String, ThymeleafController> url2ControllerMapping = new HashMap<>();
		
		url2ControllerMapping.put("/buy/", buyController);
		
		return url2ControllerMapping;
	}
	
}