package grails.plugins.selenium.events

import grails.plugins.selenium.SeleniumTestContext
import org.apache.commons.lang.StringUtils

class TestContextNotifier extends TestCaseMonitor {

	private final SeleniumTestContext context

	TestContextNotifier(SeleniumTestContext context) {
		this.context = context
	}

	boolean handles(String event) {
		event == EVENT_TEST_START || super.handles(event)
	}

	void onEvent(String event, Object... arguments) {
		if (event == EVENT_TEST_START) {
			context.selenium.context = composeContextText(arguments[0])
		} else {
			super.onEvent(event, arguments)
		}
	}

	private String composeContextText(String testName) {
		def contextText = new StringBuilder()
		use(StringUtils) {
			contextText << (currentTestCase.contains(".") ? currentTestCase.substringAfterLast(".") : currentTestCase)
		}
		contextText << "." << testName
		return contextText as String
	}

}
