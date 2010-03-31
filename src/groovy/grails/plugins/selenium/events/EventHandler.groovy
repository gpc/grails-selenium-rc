package grails.plugins.selenium.events

interface EventHandler {

	static final String EVENT_TEST_FAILURE = "TestFailure"
	static final String EVENT_TEST_START = "TestStart"
	static final String EVENT_TEST_CASE_START = "TestCaseStart"

	boolean handles(String event)
	void onEvent(String event, Object... arguments)

}
