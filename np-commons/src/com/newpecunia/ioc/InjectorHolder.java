package com.newpecunia.ioc;

import com.google.inject.Injector;

/**
 * Holds instance of the Guice injector.
 * Should be filled after initialization of Injector (after calling createInjector())
 */
public class InjectorHolder {
	
	private static Injector staticInjector;
	
	private InjectorHolder() {}
	
	public static void setInjector(Injector injector) {
		staticInjector = injector;
	}
	
	public static Injector getinjector() {
		return staticInjector;
	}

}
