package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.plugins.selenium.condition.ClosureEvaluatingWait

/**
 * Mixin for Selenium tests that provides access to the running Selenium instance, config, etc. 
 */
class SeleniumTest {

	/**
	 * Returns Selenium configuration.
	 */
	ConfigObject getConfig() {
		SeleniumManager.instance.config
	}

	/**
	 * Returns the running Selenium instance.
	 */
	Selenium getSelenium() {
		SeleniumManager.instance.selenium
	}

	/**
	 * Returns the URL context path for the application. This is required to prefix URLs, e.g. to have
	 * Selenium open the project root you would use: selenium.open("$contextPath/")
	 */
	String getContextPath() {
		def appContext = ConfigurationHolder.config.app.context ?: ApplicationHolder.application.metadata."app.name"
		if (!appContext.startsWith("/")) appContext = "/$appContext"
		return appContext
	}

	/**
	 * Waits for a condition to become true, failing if the condition does not hold within the default timeout
	 * set in Selenium config.
	 */
	void waitFor(String message = null, Closure condition) {
		def msg = message ? "Timed out waiting for: $message." : "Timed out."
		def timeout = config?.selenium?.defaultTimeout ?: Wait.DEFAULT_TIMEOUT
		new ClosureEvaluatingWait(condition: condition).wait(msg, timeout)
	}

}
