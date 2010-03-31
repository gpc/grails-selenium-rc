package grails.plugins.selenium.events

import grails.plugins.selenium.SeleniumTestContext
import org.apache.commons.lang.StringUtils

class TestContextNotifier implements EventHandler {

	private final SeleniumTestContext context
	private String currentTestCase

	TestContextNotifier(SeleniumTestContext context) {
		this.context = context
	}

	boolean handles(String event) {
		return event in [EVENT_TEST_START, EVENT_TEST_CASE_START]
	}

	void onEvent(String event, Object... arguments) {
		switch (event) {
			case EVENT_TEST_CASE_START:
				String testCaseName = arguments[0]
				use(StringUtils) {
					currentTestCase = testCaseName.contains(".") ? testCaseName.substringAfterLast(".") : testCaseName
				}
				break
			case EVENT_TEST_START:
				String testName = arguments[0]
				context.selenium.context = "${currentTestCase}.${testName}"
				break
		}
	}

}
