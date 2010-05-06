package grails.plugins.selenium

import com.thoughtworks.selenium.CommandProcessor
import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.meta.AndWaitDynamicMethod
import grails.plugins.selenium.meta.WaitForDynamicMethod
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.codehaus.groovy.grails.commons.metaclass.DynamicMethodInvocation

/**
 * Enhances a standard Selenium object with a handful of new methods.
 */
class SeleniumWrapper {

	@Delegate private final Selenium selenium

	private final CommandProcessor commandProcessor
	private final ConfigObject config

	private boolean alive = false
	private String timeout

	private static final DYNAMIC_METHODS = [new AndWaitDynamicMethod(), new WaitForDynamicMethod()]

	SeleniumWrapper(Selenium selenium, CommandProcessor commandProcessor, ConfigObject config) {
		this.selenium = selenium
		this.commandProcessor = commandProcessor
		this.config = config
	}

	String getDefaultTimeout() {
		config.selenium.defaultTimeout
	}

	String getTimeout() {
		return timeout ?: defaultTimeout
	}

	void setTimeout(String timeout) {
		this.timeout = timeout
		selenium.timeout = timeout
	}

	void waitForPageToLoad() {
		selenium.waitForPageToLoad(timeout ?: defaultTimeout)
	}

	/**
	 * Returns the application context path that should be prepended on any open call.
	 */
	String getContextPath() {
		def context = ConfigurationHolder.config.grails.app.context ?: ApplicationHolder.application.metadata."app.name"
		if (!context.startsWith("/")) context = "/$context"
		return context
	}

	/**
	 * Returns true if the Selenium session is currently active and accepting commands.
	 */
	boolean isAlive() {
		return alive
	}

	void start() {
		selenium.start()
		alive = true
	}

	void start(String configString) {
		selenium.start(configString)
		alive = true
	}

	void start(Object configObject) {
		selenium.start(configObject)
		alive = true
	}

	void stop() {
		alive = false
		selenium.stop()
	}

	def methodMissing(String methodName, args) {
		DynamicMethodInvocation method = DYNAMIC_METHODS.find { it.isMethodMatch(methodName) }
		if (method) {
			def mc = SeleniumWrapper.metaClass
			// register the method invocation for next time
			synchronized (SeleniumWrapper) {
				mc."$methodName" = {List varArgs ->
					method.invoke(delegate, methodName, varArgs)
				}
			}
			return method.invoke(this, methodName, args)
		} else if (args.every { it instanceof String }) {
			def result = commandProcessor.doCommand(methodName, args as String[])
			return StringUtils.substringAfter(result, "OK,")
		} else {
			throw new MissingMethodException(methodName, SeleniumWrapper, args)
		}
	}

}
