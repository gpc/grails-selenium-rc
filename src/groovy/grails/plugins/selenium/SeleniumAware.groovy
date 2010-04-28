package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium

/**
 * Mixin for Selenium tests / page objects that provides access to the running Selenium instance.
 */
class SeleniumAware {

	SeleniumWrapper getSelenium() {
		return SeleniumHolder.selenium
	}

}
