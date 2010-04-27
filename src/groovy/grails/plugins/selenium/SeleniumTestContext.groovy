package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait

class SeleniumTestContext {

	private final Selenium selenium
	private final ConfigObject config

	SeleniumTestContext(Selenium selenium, ConfigObject config) {
		this.selenium = selenium
		this.config = config
	}

	ConfigObject getConfig() {
		return config
	}

	Selenium getSelenium() {
		return selenium
	}

	int getTimeout() {
		return config.selenium.defaultTimeout ?: Wait.DEFAULT_TIMEOUT
	}

	int getInterval() {
		return config.selenium.defaultInterval ?: Wait.DEFAULT_INTERVAL
	}

}
