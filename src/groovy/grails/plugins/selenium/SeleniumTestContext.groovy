package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium

interface SeleniumTestContext {

	ConfigObject getConfig()
	Selenium getSelenium()
	int getTimeout()
	int getInterval()

}
