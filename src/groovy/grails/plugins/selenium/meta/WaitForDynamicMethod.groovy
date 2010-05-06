package grails.plugins.selenium.meta

import grails.plugins.selenium.condition.ClosureEvaluatingWait
import java.util.regex.Pattern
import org.codehaus.groovy.grails.commons.metaclass.AbstractDynamicMethodInvocation
import org.hamcrest.Matcher

class WaitForDynamicMethod extends AbstractDynamicMethodInvocation {

	private static final Pattern WAIT_FOR_PATTERN = ~/^waitFor(Not)?(\w+)$/

	WaitForDynamicMethod() {
		super(WAIT_FOR_PATTERN)
	}

	Object invoke(Object target, String methodName, Object[] arguments) {
		def matcher = methodName =~ WAIT_FOR_PATTERN
		def seleniumCommand = matcher[0][2]
		boolean negated = matcher[0][1] == "Not"

		def waitCondition = new ClosureEvaluatingWait()
		if (target.respondsTo("is$seleniumCommand")) {
			waitCondition.condition = {
				target."is$seleniumCommand"(* arguments) ^ negated
			}
		} else if (target.respondsTo("get$seleniumCommand")) {
			waitCondition.condition = {
				def argsList = arguments as List
				def expected = argsList.pop()
				def actual = target."get$seleniumCommand"(* argsList)
				if (expected instanceof Matcher) {
					if (negated) {
						// using waitForNot(equalTo(x)) makes no sense, use waitFor(not(equalTo(x))
						throw new MissingMethodException(methodName, target.getClass(), arguments)
					}
					expected.matches(actual)
				} else {
					actual == expected ^ negated
				}
			}
		} else {
			throw new MissingMethodException(methodName, target.getClass(), arguments)
		}
		waitCondition.wait("$methodName(${arguments.join(', ')}) timed out", target.timeout.toLong())
	}
}
