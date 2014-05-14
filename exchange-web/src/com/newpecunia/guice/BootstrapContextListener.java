package com.newpecunia.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.newpecunia.bitcoind.BitcoindConnectorModule;
import com.newpecunia.bitstamp.BitstampConnectorModule;
import com.newpecunia.common.CommonModule;
import com.newpecunia.creditcard.CreditCardConnectorModule;
import com.newpecunia.exchangeweb.serviceservlets.BuyServlet;
import com.newpecunia.exchangeweb.serviceservlets.CustomerBuyPriceServlet;
import com.newpecunia.exchangeweb.serviceservlets.UnconfirmedBuyServlet;
import com.newpecunia.ioc.InjectorHolder;
import com.newpecunia.scheduler.SchedulerModule;
import com.newpecunia.thymeleaf.ThymeleafServlet;
import com.newpecunia.trader.TraderModule;

	 
public class BootstrapContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
    	Injector injector = Guice.createInjector(
        		new BitcoindConnectorModule(),
        		new BitstampConnectorModule(),
        		new CreditCardConnectorModule(),
        		new CommonModule(),
        		new TraderModule(),
        		new JpaPersistModule("productionJpaUnit"), 
        		new SchedulerModule(),
        		new ServletModule() {
					@Override
					protected void configureServlets() {

						filter("/*").through(PersistFilter.class);
						
						//Services
						serve("/customerBuyPrice").with(CustomerBuyPriceServlet.class);
						serve("/unconfirmedBuyService").with(UnconfirmedBuyServlet.class);
						serve("/buyService").with(BuyServlet.class);
						
						
						//Thymeleaf templating
						serve("/buy/").with(ThymeleafServlet.class);
//						serve("*.html").with(DisableHtmlServlet.class); //TODO enable - temporary disabled to be able to show the payment page
					}
        		}
        	);
    	
    	InjectorHolder.setInjector(injector);
    	return InjectorHolder.getinjector();
    }
    
}	
	

