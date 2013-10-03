package com.newpecunia.ioc;

import javax.servlet.annotation.WebListener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.newpecunia.bitcoind.BitcoindConnectorModule;
import com.newpecunia.bitstamp.BitstampConnectorModule;
import com.newpecunia.common.CommonModule;
import com.newpecunia.persistence.PersistenceModule;
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
        		new UnicreditConnectorModule()
        	);
    }
}	
	

