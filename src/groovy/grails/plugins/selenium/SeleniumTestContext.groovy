package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium

interface SeleniumTestContext {

	Selenium getSelenium()
	String getCurrentTestCase()
	int getTimeout()
	int getInterval()
	boolean screenshotOnFail()

}
