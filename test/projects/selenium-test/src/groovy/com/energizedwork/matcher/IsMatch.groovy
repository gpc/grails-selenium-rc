package com.energizedwork.matcher

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class IsMatch extends TypeSafeMatcher<String> {

	static Matcher<String> isMatch(String pattern) {
		return new IsMatch(pattern)
	}

	private final String pattern

	IsMatch(String pattern) {
		this.pattern = pattern
	}

	boolean matchesSafely(String value) {
		return value ==~ pattern
	}

	void describeTo(Description description) {
		description.appendText("a String matching /").appendValue(pattern).appendText("/")
	}
}
