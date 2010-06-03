/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugins.selenium.meta

import grails.plugins.selenium.condition.ClosureEvaluatingWait
import java.util.regex.Pattern
import org.codehaus.groovy.grails.commons.metaclass.AbstractDynamicMethodInvocation
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import static grails.plugins.selenium.condition.PatternMatcher.matchesPattern
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.not

class WaitForDynamicMethod extends AbstractDynamicMethodInvocation {

	private static final Pattern WAIT_FOR_PATTERN = ~/^waitFor(Not)?(\w+)$/

	WaitForDynamicMethod() {
		super(WAIT_FOR_PATTERN)
	}

	Object invoke(Object target, String methodName, Object[] arguments) {
		def m = methodName =~ WAIT_FOR_PATTERN
		def seleniumCommand = m[0][2]
		boolean negated = m[0][1] == "Not"
		List argsList = arguments as List

		Matcher matcher
		if (target.respondsTo("is$seleniumCommand")) {
			seleniumCommand = "is$seleniumCommand"
			matcher = equalTo(!negated)
		} else if (target.respondsTo("get$seleniumCommand")) {
			seleniumCommand = "get$seleniumCommand"
			def expected = argsList.pop()
			matcher = getMatcher(expected, negated)
		}

		if (!matcher) {
			throw new MissingMethodException(methodName, target.getClass(), arguments)
		}

		def waitCondition = new ClosureEvaluatingWait()
		waitCondition.condition = {
			def actual = target."$seleniumCommand"(* argsList)
			matcher.matches(actual)
		}
		waitCondition.wait(getMessage(seleniumCommand, argsList, matcher), target.timeout.toLong())
		return true
	}

	private String getMessage(String seleniumCommand, List argsList, Matcher matcher) {
		def description = new StringDescription()
		description.appendText("Timed out waiting for ")
		description.appendText(seleniumCommand).appendText("(").appendText(argsList.join(", ")).append(")")
		description.appendText(" to be ")
		description.appendDescriptionOf(matcher)
		return description.toString()
	}

	private Matcher getMatcher(expected, boolean negated) {
		Matcher matcher
		if (expected instanceof Matcher) {
			if (negated) {
				return null
			}
			matcher = expected
		} else if (expected instanceof Pattern) {
			matcher = matchesPattern(expected)
		} else {
			matcher = equalTo(expected)
		}
		if (negated) matcher = not(matcher)
		return matcher
	}
}
