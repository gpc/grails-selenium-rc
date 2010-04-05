package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium

interface SeleniumTestContext {

	ConfigObject getConfig() // TODO: really needed?
	Selenium getSelenium()
	int getTimeout()
	int getInterval()

	// TODO: remove from interface
	void setConfig(ConfigObject config)

}
