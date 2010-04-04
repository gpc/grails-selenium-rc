package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumTestContext

class SeleniumRunner {

	private final SeleniumTestContext context
	private Selenium selenium

	SeleniumRunner(SeleniumTestContext context) {
		this.context = context
	}

	void startSelenium() {
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

	void stopSelenium() {
		selenium?.stop()
		context.selenium = null
	}
}
