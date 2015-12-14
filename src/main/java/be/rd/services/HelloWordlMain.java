package be.rd.services;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import be.rd.services.beans.MessageWriter;
import be.rd.services.interceptors.MessageDecorator;
import be.rd.services.interceptors.MethodLoggingDecorator;

public class HelloWordlMain {

	public static void main(String[] args) {

		MessageWriter mw = new MessageWriter();
		MessageDecorator md = new MessageDecorator();
		MethodLoggingDecorator mld = new MethodLoggingDecorator();
		
		MessageWriter proxiedMw = createProxyDecoratorForClass(mw, md);
		proxiedMw.writeMsg();
		
		proxiedMw = createProxyDecoratorForClass(mw, mld);
		proxiedMw.writeMsg();
		proxiedMw.toString();
		
		// following samples only advises 1 method		
		// --> the toString will not have methodlogging
		
		//1. using DefaultPointCutAdvisor
		Advisor advisor = new DefaultPointcutAdvisor(new StaticMethodMatcherPointcut() {
			
			@Override
			public boolean matches(Method method, Class<?> targetClass) {
				return "writeMsg".equals(method.getName());
			}
		}, mld);
		proxiedMw = createProxyDecoratorForClass(mw, advisor);
		proxiedMw.writeMsg();
		proxiedMw.toString();
		
		//1. using NameMatchMethodPointcutAdvisor
		NameMatchMethodPointcutAdvisor methodAdvisor = new NameMatchMethodPointcutAdvisor(mld);
		methodAdvisor.addMethodName("writeMsg");
		proxiedMw = createProxyDecoratorForClass(mw, methodAdvisor);
		proxiedMw.writeMsg();
		proxiedMw.toString();
	}

	/**
	 * This will decorate all methods on the class.
	 * (uses addAdvice is same as invoking addAdvisor with the defaultPointCutAdvisor)
	 */
	private static <T> T createProxyDecoratorForClass(T clazzToDecorate, Advice decorator){
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(clazzToDecorate);
		pf.addAdvice(decorator);
		return (T)pf.getProxy();		
	}

	/**
	 * This can work with a specific pointCutAdvisor meaning
	 * you can specify which parts of the class must be decorated.
	 * For instance only advise 1 method of the class ...  
	 */
	private static <T> T createProxyDecoratorForClass(T clazzToDecorate, Advisor advisor){
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(clazzToDecorate);
		pf.addAdvisor(advisor);
		return (T)pf.getProxy();		
	}
}
