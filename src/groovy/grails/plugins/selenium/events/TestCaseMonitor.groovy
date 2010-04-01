package grails.plugins.selenium.events

class TestCaseMonitor extends EventHandlerSupport {

	private String currentTestCase

	TestCaseMonitor() {
		super(EventHandler.EVENT_TEST_CASE_START)
	}

	TestCaseMonitor(Collection<String> handledEvents) {
		super(handledEvents + EventHandler.EVENT_TEST_CASE_START)
	}

	TestCaseMonitor(String handledEvent) {
		super([handledEvent, EventHandler.EVENT_TEST_CASE_START])
	}

	protected String getCurrentTestCase() { currentTestCase }

	void onEvent(String event, Object... arguments) {
		currentTestCase = arguments[0]
	}

}
