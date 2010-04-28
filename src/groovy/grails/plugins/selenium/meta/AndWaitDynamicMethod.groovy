package grails.plugins.selenium.meta

import java.util.regex.Pattern
import org.codehaus.groovy.grails.commons.metaclass.AbstractDynamicMethodInvocation

class AndWaitDynamicMethod extends AbstractDynamicMethodInvocation {

	private static final Pattern AND_WAIT_PATTERN = ~/^(\w+)AndWait$/

	AndWaitDynamicMethod() {
		super(AND_WAIT_PATTERN)
	}

	Object invoke(Object target, String methodName, Object[] arguments) {
		String decoratedSeleniumMethod = methodName.find(AND_WAIT_PATTERN) { match, name -> name }
		target."$decoratedSeleniumMethod"(* arguments)
		target.waitForPageToLoad(target.timeout)
	}
}
