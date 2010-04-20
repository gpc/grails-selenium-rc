package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium

class SeleniumTestContextHolder {

	static SeleniumTestContext context

	private SeleniumTestContextHolder() {}

	static void initialise(Selenium selenium, ConfigObject config) {
		context = new SeleniumTestContext(selenium, config)
	}

	static void clear() {
		context = null
	}

}
