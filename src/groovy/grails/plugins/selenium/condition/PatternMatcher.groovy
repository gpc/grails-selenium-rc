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
