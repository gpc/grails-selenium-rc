package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.SeleniumTestContext
import grails.plugins.selenium.events.EventHandlerSupport
import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.Selenium

class SeleniumRunner extends EventHandlerSupport {

	private Selenium selenium

	SeleniumRunner(SeleniumTestContext context) {
		super(context, [])
	}

	def void onEvent(String event, Object... arguments) {
		switch (event) {
			case "start":
				startSelenium()
				break
			case "stop":
				stopSelenium()
				break
		}
	}

	private void startSelenium() {
		def host = context.config.selenium.server.host
		def port = context.config.selenium.server.port
		def browser = context.config.selenium.browser
		def url = context.config.selenium.url
		def maximize = context.config.selenium.windowMaximize

		selenium = new DefaultSelenium(host, port, browser, url)
		selenium.start()
		if (maximize) {
			selenium.windowMaximize()
		}
		
		context.selenium = selenium
	}

	private void stopSelenium() {
		selenium?.stop()
		context.selenium = null
	}
}
