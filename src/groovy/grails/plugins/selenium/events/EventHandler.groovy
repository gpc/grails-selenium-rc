package grails.plugins.selenium.events

interface EventHandler {

	static final String EVENT_TEST_FAILURE = "TestFailure"
	static final String EVENT_TEST_START = "TestStart"
	static final String EVENT_TEST_CASE_START = "TestCaseStart"
	static final String EVENT_TEST_SUITE_START = "TestSuiteStart"
	static final String EVENT_TEST_SUITE_END = "TestSuiteEnd"

	boolean handles(String event)
	void onEvent(String event, Object... arguments)

}
