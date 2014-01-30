package com.newpecunia.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.newpecunia.bitcoind.BitcoindConnectorModule;
import com.newpecunia.bitcoind.service.impl.BitcoindReceivedPaymentCallbackServlet;
import com.newpecunia.bitstamp.BitstampConnectorModule;
import com.newpecunia.common.CommonModule;
import com.newpecunia.creditcard.CreditCardConnectorModule;
import com.newpecunia.exchangeweb.serviceservlets.BuyServlet;
import com.newpecunia.exchangeweb.serviceservlets.CustomerBuySellPriceServlet;
import com.newpecunia.ioc.InjectorHolder;
import com.newpecunia.persistence.PersistenceModule;
import com.newpecunia.scheduler.SchedulerModule;
import com.newpecunia.thymeleaf.DisableHtmlServlet;
import com.newpecunia.thymeleaf.ThymeleafServlet;
import com.newpecunia.trader.TraderModule;
import com.newpecunia.unicredit.UnicreditConnectorModule;

	 
public class BootstrapContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
    	Injector injector = Guice.createInjector(
        		new BitcoindConnectorModule(),
        		new BitstampConnectorModule(),
        		new CreditCardConnectorModule(),
        		new CommonModule(),
        		new PersistenceModule(),
        		new TraderModule(),
        		new UnicreditConnectorModule(),        		
        		new JpaPersistModule("productionJpaUnit"), 
        		new SchedulerModule(),
        		new ServletModule() {
					@Override
					protected void configureServlets() {
					    filter("/*").through(PersistFilter.class);
						serve("/bitcoindReceivedPaymentCallback_dgqac0akerd1c4e7asiy5d8zqjdg68652u").with(BitcoindReceivedPaymentCallbackServlet.class); //the address has non-guesable postfix to prevent calling it by some attacker
//						serve("/test").with(TestServlet.class); //TODO remove before going into production!
						
						//Services
						serve("/customerBuySellPrice").with(CustomerBuySellPriceServlet.class);
						serve("/buyService").with(BuyServlet.class);
						
						
						//Thymeleaf templating
						serve("/buy/", "/sell/").with(ThymeleafServlet.class);
//						serve("*.html").with(DisableHtmlServlet.class); //TODO enable - temporary disablet to be able to show the payment page
					}
        		}
        	);
    	
    	InjectorHolder.setInjector(injector);
    	return InjectorHolder.getinjector();
    }
    
}	
	

