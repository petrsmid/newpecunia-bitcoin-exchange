package com.newpecunia.guice;

import javax.servlet.annotation.WebListener;

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
import com.newpecunia.persistence.PersistenceModule;
import com.newpecunia.test.TestServlet;
import com.newpecunia.trader.TraderModule;
import com.newpecunia.unicredit.UnicreditConnectorModule;
	 
@WebListener
public class BootstrapContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
        		new BitcoindConnectorModule(),
        		new BitstampConnectorModule(),
        		new CommonModule(),
        		new PersistenceModule(),
        		new TraderModule(),
        		new UnicreditConnectorModule(),        		
        		new JpaPersistModule("productionJpaUnit"),        		
        		new ServletModule() {
					@Override
					protected void configureServlets() {
					    filter("/*").through(PersistFilter.class);
						serve("/bitcoindReceivedPaymentCallback_dgqac0akerd1c4e7asiy5d8zqjdg68652u").with(BitcoindReceivedPaymentCallbackServlet.class); //the address has non-guesable postfix to prevent calling it by some attacker
						serve("/test").with(TestServlet.class); //TODO remove before going into production!
					}
        		}
        	);
    }
    
}	
	
