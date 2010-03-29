package grails.plugins.selenium.meta

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumManager
import org.codehaus.groovy.grails.commons.metaclass.DynamicMethodInvocation

class SeleniumDynamicMethods {

	private static final AND_WAIT_PATTERN = /^(\w+)AndWait$/

	static void enhanceSelenium() {
		def mc = Selenium.metaClass
		def dynamicMethods = [new AndWaitDynamicMethod(), new WaitForDynamicMethod()]

		mc.methodMissing = {String methodName, args ->
			DynamicMethodInvocation method = dynamicMethods.find {it.isMethodMatch(methodName)}
			if (method) {
				// register the method invocation for next time
				synchronized (SeleniumDynamicMethods) {
					mc.static."$methodName" = {List varArgs ->
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
