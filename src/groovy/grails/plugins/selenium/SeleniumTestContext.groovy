package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium

interface SeleniumTestContext {

	ConfigObject getConfig()
	void setConfig(ConfigObject config)
	Selenium getSelenium()
	void setSelenium(Selenium selenium)
	int getTimeout()
	int getInterval()

}
