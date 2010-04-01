package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.SeleniumTestContext
import org.apache.commons.lang.StringUtils
import grails.plugins.selenium.events.TestCaseMonitor
import grails.plugins.selenium.events.EventHandler

class TestContextNotifier extends TestCaseMonitor {

	private final SeleniumTestContext context

	TestContextNotifier(SeleniumTestContext context) {
		super(EventHandler.EVENT_TEST_START)
		this.context = context
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
