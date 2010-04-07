package grails.plugins.selenium.meta

import grails.plugins.selenium.condition.ClosureEvaluatingWait
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
				def expected = arguments[-1]
				def actual = target."get$seleniumCommand"(* arguments[0..-2])
				if (expected instanceof Matcher) {
					expected.matches(actual)
				} else {
					actual == expected
				}
			}
		} else {
			throw new MissingMethodException(methodName, target.getClass(), arguments)
		}
		waitCondition.wait("$methodName(${arguments.join(', ')}) timed out", SeleniumManager.instance.timeout, SeleniumManager.instance.interval)
	}
}
