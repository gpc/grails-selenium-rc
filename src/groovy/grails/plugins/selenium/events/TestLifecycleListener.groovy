package grails.plugins.selenium.events

import grails.build.GrailsBuildListener
import grails.plugins.selenium.SeleniumTestContextHolder

abstract class TestLifecycleListener implements GrailsBuildListener {

	public static final String EVENT_TEST_FAILURE = "TestFailure"
	public static final String EVENT_TEST_START = "TestStart"
	public static final String EVENT_TEST_CASE_START = "TestCaseStart"

	private String currentTestCaseName

	void receiveGrailsBuildEvent(String eventName, Object... args) {
		if (SeleniumTestContextHolder.context == null) return
		switch (eventName) {
			case EVENT_TEST_CASE_START:
				currentTestCaseName = args[0]
				break
			case EVENT_TEST_START:
				String testName = args[0]
				onTestStart(currentTestCaseName, testName)
				break
			case EVENT_TEST_FAILURE:
				String testName = args[0]
				onTestFailure(currentTestCaseName, testName)
				break
		}
	}

	protected void onTestStart(String testCaseName, String testName) { }

	protected void onTestFailure(String testCaseName, String testName) { }

}
