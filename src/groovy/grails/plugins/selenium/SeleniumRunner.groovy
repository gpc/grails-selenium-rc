package grails.plugins.selenium

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.HttpCommandProcessor

class SeleniumRunner {

	SeleniumWrapper startSelenium(ConfigObject seleniumConfig) {
		def host = seleniumConfig.selenium.server.host
		def port = seleniumConfig.selenium.server.port
		def browser = seleniumConfig.selenium.browser
		def url = seleniumConfig.selenium.url
		def maximize = seleniumConfig.selenium.windowMaximize

		def commandProcessor = new HttpCommandProcessor(host, port, browser, url)
		def selenium = new DefaultSelenium(commandProcessor)
		
		SeleniumHolder.selenium = new SeleniumWrapper(selenium, commandProcessor, seleniumConfig)
		SeleniumHolder.selenium.start()
		if (maximize) {
			SeleniumHolder.selenium.windowMaximize()
		}

		return SeleniumHolder.selenium
	}

	void stopSelenium() {
		SeleniumHolder.selenium?.stop()
		SeleniumHolder.selenium = null
	}

}
