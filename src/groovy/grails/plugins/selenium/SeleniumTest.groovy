package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

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
	 * <tt>selenium</tt> open the project root you would use:<pre>selenium.open("$contextPath/")</pre>
	 */
	String getContextPath() {
		def appContext = ConfigurationHolder.config.app.context ?: ApplicationHolder.application.metadata."app.name"
		if (!appContext.startsWith("/")) appContext = "/$appContext"
		return appContext
	}
	
	/**
	 * Waits for a condition to become true, failing if the condition does not hold within the default timeout
	 * set on the <tt>selenium</tt>.
	 */
	void waitFor(String message = null, Closure condition) {
		def waitCondition = new ClosureEvaluatingWait()
		waitCondition.condition = condition

		def msg = message ? "Timed out waiting for: $message." : "Timed out."
		def timeout = config?.selenium?.defaultTimeout ?: Wait.DEFAULT_TIMEOUT
		
		waitCondition.wait(msg, timeout)
	}

}
