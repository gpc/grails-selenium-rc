package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait
import grails.build.GrailsBuildListener
import grails.plugins.selenium.events.EventHandler
import grails.plugins.selenium.lifecycle.ScreenshotGrabber
import grails.plugins.selenium.lifecycle.TestContextNotifier
import org.codehaus.groovy.grails.cli.support.GrailsBuildEventListener
import org.slf4j.LoggerFactory

class SeleniumManager implements SeleniumTestContext, GrailsBuildListener {

	private static final log = LoggerFactory.getLogger(SeleniumManager)
	private final Collection<EventHandler> eventHandlers = []
	Selenium selenium
	ConfigObject config

	static void initialize(ConfigObject seleniumConfig, GrailsBuildEventListener eventListener) {
		def seleniumManager = new SeleniumManager()
		seleniumManager.config = seleniumConfig
		seleniumManager.eventHandlers << new ScreenshotGrabber(seleniumManager)
		seleniumManager.eventHandlers << new TestContextNotifier(seleniumManager)
		eventListener.addGrailsBuildListener(seleniumManager)
		SeleniumTestContextHolder.context = seleniumManager
	}

	Selenium getSelenium() {
		return selenium
	}

	ConfigObject getConfig() {
		return config
	}

	int getTimeout() {
		return config?.selenium?.defaultTimeout ?: Wait.DEFAULT_TIMEOUT
	}

	int getInterval() {
		return config?.selenium?.defaultInterval ?: Wait.DEFAULT_INTERVAL
	}

	void receiveGrailsBuildEvent(String event, Object... args) {
		eventHandlers.each {
			if (it.handles(event)) {
				it.onEvent(event, args)
			}
		}
	}
}
