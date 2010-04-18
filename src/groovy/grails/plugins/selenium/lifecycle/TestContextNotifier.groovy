package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.SeleniumTestContextHolder
import grails.plugins.selenium.events.TestLifecycleListener
import org.apache.commons.lang.StringUtils

class TestContextNotifier extends TestLifecycleListener {

	void onTestStart(String testCaseName, String testName) {
		SeleniumTestContextHolder.context.selenium.context = composeContextText(testCaseName, testName)
	}

	private String composeContextText(String testCaseName, String testName) {
		def contextText = new StringBuilder()
		use(StringUtils) {
			contextText << (testCaseName.contains(".") ? testCaseName.substringAfterLast(".") : testCaseName)
		}
		contextText << "." << testName
		return contextText as String
	}

}
