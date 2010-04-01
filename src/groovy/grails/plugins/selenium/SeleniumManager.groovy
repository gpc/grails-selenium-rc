package grails.plugins.selenium

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait
import grails.build.GrailsBuildListener
import grails.plugins.selenium.events.EventHandler
import grails.plugins.selenium.lifecycle.ScreenshotGrabber
import grails.plugins.selenium.lifecycle.SeleniumServerRunner
import grails.plugins.selenium.lifecycle.TestContextNotifier
import org.slf4j.LoggerFactory

@Singleton class SeleniumManager implements SeleniumTestContext, GrailsBuildListener {

	private static final log = LoggerFactory.getLogger(SeleniumManager)

	private def seleniumServer
	ConfigObject config
	Selenium selenium
	private final Collection<EventHandler> eventHandlers = []

	private SeleniumManager() {
		// TODO: passing refs to this shouldn't be done in constructor
		eventHandlers << new SeleniumServerRunner(this)
		eventHandlers << new ScreenshotGrabber(this)
		eventHandlers << new TestContextNotifier(this)
	}

	void startSelenium(serverURL) {
		def host = config.selenium.server.host
		def port = config.selenium.server.port
		def browser = config.selenium.browser
		def url = config.selenium.url ?: serverURL
		selenium = new DefaultSelenium(host, port, browser, url)

		selenium.start()
		if (config.selenium.windowMaximize) {
			selenium.windowMaximize()
		}
	}

	void stopSelenium() {
		selenium?.stop()
		selenium = null
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
