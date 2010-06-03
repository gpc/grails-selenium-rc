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
