package com.newpecunia.aoplogging;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Interceptor able to log service calls. We can use it in future to log all service calls (possibly to DB)
 */
public class AuditLogInterceptor implements MethodInterceptor {
	
	private static final Logger logger = LogManager.getLogger(AuditLogInterceptor.class);		
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object returnValue = null;
		Throwable exception = null;
		try {
			returnValue = invocation.proceed();
		} catch (Throwable t) {
			exception = t;
			throw t;
		} finally {
			//TODO - implement custom logging - e.g. with request and response / exception transformed to JSON and saved into DB or MongoDB
			logger.trace(String.format("Called %s.%s()", invocation.getThis().getClass().getName(), invocation.getMethod().getName()));
		}

		return returnValue;
	}
}