package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.SeleniumTestContextHolder
import grails.plugins.selenium.events.TestLifecycleListener
import static grails.util.GrailsNameUtils.getShortName

class TestContextNotifier extends TestLifecycleListener {

	void onTestStart(String testCaseName, String testName) {
		SeleniumTestContextHolder.context.selenium.showContextualBanner(getShortName(testCaseName), testName)
	}

}
