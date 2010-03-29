package grails.plugins.selenium.meta

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumManager
import org.codehaus.groovy.grails.commons.metaclass.DynamicMethodInvocation

class SeleniumDynamicMethods {

	static void enhanceSelenium() {
		def mc = Selenium.metaClass
		def dynamicMethods = [new AndWaitDynamicMethod(), new WaitForDynamicMethod()]

		mc.methodMissing = {String methodName, args ->
			println "hit method missing"
			DynamicMethodInvocation method = dynamicMethods.find {it.isMethodMatch(methodName)}
			if (method) {
				// register the method invocation for next time
				synchronized (SeleniumDynamicMethods) {
					mc."$methodName" = {List varArgs ->
						method.invoke(delegate, methodName, varArgs)
					}
				}
				return method.invoke(delegate, methodName, args)
			}
			else {
				throw new MissingMethodException(methodName, delegate.getClass(), args)
			}
		}
	}

	private static int getTimeout() {
		return SeleniumManager.instance.config.selenium.defaultTimeout
	}

}
