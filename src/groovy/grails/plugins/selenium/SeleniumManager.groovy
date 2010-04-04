package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait
import grails.build.GrailsBuildListener
import grails.plugins.selenium.events.EventHandler
import grails.plugins.selenium.lifecycle.ScreenshotGrabber
import grails.plugins.selenium.lifecycle.SeleniumServerRunner
import grails.plugins.selenium.lifecycle.TestContextNotifier
import org.slf4j.LoggerFactory
import grails.plugins.selenium.lifecycle.SeleniumRunner

@Singleton class SeleniumManager implements SeleniumTestContext, GrailsBuildListener {

	private static final log = LoggerFactory.getLogger(SeleniumManager)
	private final Collection<EventHandler> eventHandlers = []
	ConfigObject config
	Selenium selenium

	private SeleniumManager() {
		// TODO: passing refs to this shouldn't be done in constructor
		eventHandlers << new SeleniumServerRunner(this)
		eventHandlers << new SeleniumRunner(this)
		eventHandlers << new ScreenshotGrabber(this)
		eventHandlers << new TestContextNotifier(this)
	}

	int getTimeout() {
		config?.selenium?.defaultTimeout ?: Wait.DEFAULT_TIMEOUT
	}

	int getInterval() {
		config?.selenium?.defaultInterval ?: Wait.DEFAULT_INTERVAL
	}

	void receiveGrailsBuildEvent(String event, Object... args) {
		println event
		eventHandlers.each {
			if (it.handles(event)) {
				it.onEvent(event, args)
			}
		}
	}
}
