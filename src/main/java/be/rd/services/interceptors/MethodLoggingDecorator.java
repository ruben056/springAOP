package be.rd.services.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodLoggingDecorator implements MethodInterceptor {

	
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		doStartMethodLogging(invocation);

		Object result = invocation.proceed();
		
		doEndMethodLogging(invocation);
		return result;
	}
	
	private void doEndMethodLogging(MethodInvocation invocation) {
		String msg = invocation.getMethod().getName() + " ended";
		getLogger(invocation).info(msg);
	}

	private void doStartMethodLogging(MethodInvocation invocation) {
		String msg = invocation.getMethod().getName() + " started";
		getLogger(invocation).info(msg);
	}

	private Logger getLogger(MethodInvocation invocation){
		return LoggerFactory.getLogger(invocation.getMethod().getDeclaringClass());
	}

}
