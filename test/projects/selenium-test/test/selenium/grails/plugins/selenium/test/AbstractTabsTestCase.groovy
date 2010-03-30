package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleniumTestCase
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.BaseMatcher
import org.hamcrest.Description

/**
 * Previously had an issue where using another class between GrailsSeleniumTestCase and the actual test would cause a
 * StackOverflowError when setUp was called. This prevents a regression.
 */
abstract class AbstractTabsTestCase extends GrailsSeleniumTestCase {

	protected void assertTabSelected(int i) {
		assertThat selenium.getAttribute("//div[@id='tabs']/ul/li[$i]@class"), isMatch(/\bui-tabs-selected\b/)
	}

	static Matcher<String> isMatch(pattern) {
		return new RegexMatcher(pattern)
	}
}

class RegexMatcher extends BaseMatcher<String> {

	private final String pattern

	RegexMatcher(String pattern) {
		this.pattern = pattern
	}

	boolean matches(Object o) {
		return (o =~ pattern) != null
	}

	void describeTo(Description description) {
		description.appendText("matches regular expression: ")
	}
}