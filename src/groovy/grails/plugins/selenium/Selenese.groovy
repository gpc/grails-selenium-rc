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
	 * Returns the URL context path for the application. This is required to prefix URLs, e.g. to have
	 * <tt>selenium</tt> open the project root you would use:<pre>selenium.open("$contextPath/")</pre>
	 */
	String getContextPath() {
		return "/${ConfigurationHolder.config.web.app.context.path ?: ApplicationHolder.application.metadata."app.name"}"
	}
	
	/**
	 * Waits for a condition to become true, failing if the condition does not hold within the default timeout
	 * set on the <tt>selenium</tt>.
	 */
	void waitFor(String message = null, Closure condition) {
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
		
		Assert.fail message ? "Timed out waiting for: $message." : "Timed out."
	}

}
