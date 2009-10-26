package grails.plugins.selenium

import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import junit.framework.Assert

/**
 * Mixin for Selenese tests that provides access to the running Selenium instance, config, etc. 
 */
class Selenese {

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

	void waitFor(Closure condition) {
		def timeoutTime = System.currentTimeMillis() + selenium.defaultTimeout
		while (System.currentTimeMillis() < timeoutTime) {
			try {
				if (condition.call()) {
					return
				}
			}
			catch (e) {}
			sleep(500)
		}

		Assert.fail "timeout"
	}

}
