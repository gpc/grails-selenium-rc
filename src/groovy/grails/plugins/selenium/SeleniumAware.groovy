package grails.plugins.selenium

/**
 * Mixin for Selenium tests / page objects that provides access to the running Selenium instance.
 */
class SeleniumAware {

	/**
	 * Returns the running Selenium instance.
	 */
	SeleniumWrapper getSelenium() {
		return SeleniumHolder.selenium
	}

}
