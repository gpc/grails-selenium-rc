package grails.plugins.selenium

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait
import grails.build.GrailsBuildListener
import grails.plugins.selenium.events.EventHandler
import grails.plugins.selenium.lifecycle.DefaultSeleniumServerRunner
import grails.plugins.selenium.lifecycle.ScreenshotGrabber
import grails.plugins.selenium.lifecycle.SeleniumServerRunner
import grails.plugins.selenium.lifecycle.TestContextNotifier
import org.codehaus.groovy.grails.cli.support.GrailsBuildEventListener
import org.slf4j.LoggerFactory

class SeleniumManager implements SeleniumTestContext, GrailsBuildListener {

	private static final log = LoggerFactory.getLogger(SeleniumManager)
	private final Collection<EventHandler> eventHandlers = []
	private SeleniumServerRunner seleniumServerRunner
	private Selenium selenium
	ConfigObject config

	// TODO: clean up this evil singleton stuff
	private static SeleniumTestContext instance
	static SeleniumTestContext getInstance() {
		if (!instance) {
			throw new IllegalStateException("Selenium test context is not initialized")
		}
		return instance
	}

	static void initialize(ConfigObject seleniumConfig, GrailsBuildEventListener eventListener) {
		instance = new SeleniumManager()
		instance.config = seleniumConfig
		instance.seleniumServerRunner = new DefaultSeleniumServerRunner(instance)
		instance.eventHandlers << new ScreenshotGrabber(instance)
		instance.eventHandlers << new TestContextNotifier(instance)
		eventListener.addGrailsBuildListener(instance)
	}

	Selenium getSelenium() {
		return selenium
	}

	int getTimeout() {
		return config?.selenium?.defaultTimeout ?: Wait.DEFAULT_TIMEOUT
	}

	int getInterval() {
		return config?.selenium?.defaultInterval ?: Wait.DEFAULT_INTERVAL
	}

	void receiveGrailsBuildEvent(String event, Object... args) {
		notifyEventHandlers(event, args)

		switch (event) {
			case EventHandler.EVENT_TEST_SUITE_START:
				if (isSeleniumSuite(args[0])) {
					onSeleniumSuiteStart()
				}
				break
			case EventHandler.EVENT_TEST_SUITE_END:
				if (isSeleniumSuite(args[0])) {
					onSeleniumSuiteEnd()
				}
				break
		}
	}

	private boolean isSeleniumSuite(String testType) {
		return testType ==~ /selenium/
	}

	private void onSeleniumSuiteStart() {
		seleniumServerRunner.startServer()
		startSelenium()
	}

	private void onSeleniumSuiteEnd() {
		stopSelenium()
		seleniumServerRunner.stopServer()
	}

	void startSelenium() {
		def host = config.selenium.server.host
		def port = config.selenium.server.port
		def browser = config.selenium.browser
		def url = config.selenium.url
		def maximize = config.selenium.windowMaximize

		selenium = new DefaultSelenium(host, port, browser, url)
		selenium.start()
		if (maximize) {
			selenium.windowMaximize()
		}
	}

	void stopSelenium() {
		selenium?.stop()
		selenium = null
	}

	private void notifyEventHandlers(String event, Object... args) {
		eventHandlers.each {
			if (it.handles(event)) {
				it.onEvent(event, args)
			}
		}
	}

}
