package grails.plugins.selenium

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class SeleneseTestCategory {

	/**
	 * Returns Selenium configuration.
	 */
	ConfigObject getConfig() {
		SeleniumManager.instance.config
	}

	/**
	 * Returns the running Selenium instance.
	 */
	GrailsSelenium getSelenium() {
		SeleniumManager.instance.selenium
	}

	/**
	 * Returns the URL context path for the application.
	 */
	String getContextPath() {
		return "/${ConfigurationHolder.config.web.app.context.path ?: ApplicationHolder.application.metadata."app.name"}"
	}

}
