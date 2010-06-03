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

package grails.plugins.selenium.condition

import java.util.regex.Pattern
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class PatternMatcher extends TypeSafeMatcher<String> {

	static Matcher<String> matchesPattern(Pattern expectedPattern) {
		return new PatternMatcher(expectedPattern)
	}

	private final Pattern expectedPattern

	PatternMatcher(Pattern expectedPattern) {
		this.expectedPattern = expectedPattern
	}

	boolean matchesSafely(String item) {
		return item ==~ expectedPattern
	}

	void describeTo(Description description) {
		description.appendText("a string matching the regular expression $expectedPattern")
	}
}
