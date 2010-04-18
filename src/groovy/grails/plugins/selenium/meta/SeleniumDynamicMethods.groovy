package grails.plugins.selenium.meta

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumTestContextHolder
import org.codehaus.groovy.grails.commons.metaclass.DynamicMethodInvocation

class SeleniumDynamicMethods {

	static void enhanceSelenium() {
		def mc = Selenium.metaClass
		enhanceWithDynamicMethods(mc)
		enhanceWithOverriddenMethods(mc)
	}

	private static void enhanceWithDynamicMethods(mc) {
		def dynamicMethods = [new AndWaitDynamicMethod(), new WaitForDynamicMethod()]
		mc.methodMissing = {String methodName, args ->
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

	private static void enhanceWithOverriddenMethods(MetaClass mc) {
		mc.waitForPageToLoad = {->
			delegate.waitForPageToLoad("$timeout")
		}
	}

	private static int getTimeout() {
		return SeleniumTestContextHolder.context.config.selenium.defaultTimeout
	}

}
