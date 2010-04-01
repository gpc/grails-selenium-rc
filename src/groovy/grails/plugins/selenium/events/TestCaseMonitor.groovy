package grails.plugins.selenium.events

import grails.plugins.selenium.SeleniumTestContext

class TestCaseMonitor extends EventHandlerSupport {

	private String currentTestCase

	TestCaseMonitor(SeleniumTestContext context) {
		super(context, EventHandler.EVENT_TEST_CASE_START)
	}

	TestCaseMonitor(SeleniumTestContext context, Collection<String> handledEvents) {
		super(context, handledEvents + EventHandler.EVENT_TEST_CASE_START)
	}

	TestCaseMonitor(SeleniumTestContext context, String handledEvent) {
		super(context, [handledEvent, EventHandler.EVENT_TEST_CASE_START])
	}

	protected String getCurrentTestCase() { currentTestCase }

	void onEvent(String event, Object... arguments) {
		currentTestCase = arguments[0]
	}

}
