package grails.plugins.selenium.events

import static grails.plugins.selenium.events.EventHandler.*

class TestCaseMonitor implements EventHandler {

	private String currentTestCase

	protected String getCurrentTestCase() { currentTestCase }

	boolean handles(String event) {
		event == EVENT_TEST_CASE_START
	}

	void onEvent(String event, Object... arguments) {
		currentTestCase = arguments[0]
	}

}
