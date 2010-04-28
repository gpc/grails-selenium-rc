package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.SeleniumWrapper
import grails.plugins.selenium.events.TestLifecycleListener
import static grails.util.GrailsNameUtils.getShortName

class TestContextNotifier extends TestLifecycleListener {

	TestContextNotifier(SeleniumWrapper selenium) {
		super(selenium)
	}

	void onTestStart(String testCaseName, String testName) {
		selenium.showContextualBanner(getShortName(testCaseName), testName)
	}

}
