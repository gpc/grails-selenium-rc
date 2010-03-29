package grails.plugins.selenium.meta

import grails.plugins.selenium.ClosureEvaluatingWait
import grails.plugins.selenium.SeleniumManager
import java.util.regex.Pattern
import org.codehaus.groovy.grails.commons.metaclass.AbstractDynamicMethodInvocation

class WaitForDynamicMethod extends AbstractDynamicMethodInvocation {

	private static final Pattern WAIT_FOR_PATTERN = ~/^waitFor(\w+)$/

	WaitForDynamicMethod() {
		super(WAIT_FOR_PATTERN)
	}

	Object invoke(Object target, String methodName, Object[] arguments) {
		def seleniumCommand = methodName.find(WAIT_FOR_PATTERN) { match, name -> name }

		def waitCondition = new ClosureEvaluatingWait()
		if (target.respondsTo("is$seleniumCommand")) {
			waitCondition.condition = {
				target."is$seleniumCommand"(* arguments)
			}
		} else if (target.respondsTo("get$seleniumCommand")) {
			waitCondition.condition = {
				target."get$seleniumCommand"(* arguments[0..-2]) == arguments[-1]
			}
		} else {
			throw new MissingMethodException(methodName, target.getClass(), arguments)
		}
		waitCondition.wait("$methodName(${arguments.join(', ')}) timed out", timeout, interval)
	}

	private static int getTimeout() {
		return SeleniumManager.instance.config.selenium.defaultTimeout
	}

	private static int getInterval() {
		100
	}

}
