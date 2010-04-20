import com.thoughtworks.selenium.DefaultSelenium

selenium = null

target(registerSeleniumTestType: "Registers the selenium test type with the appropriate test phase") {
	depends(loadSeleniumConfig)

	def phase = "functional"
	if (seleniumConfig.selenium.remote) {
		event "StatusUpdate", ["Running Selenium in remote mode"]
		phase = "other"
	}

	event "StatusUpdate", ["Selenium tests will run in the ${phase} phase"]
	binding."${phase}Tests" << "selenium"

	if (binding.variables.containsKey("spockPluginDir")) {
		def specTestTypeClass = loadSpecTestTypeClass()
		binding."${phase}Tests" << specTestTypeClass.newInstance("spock-selenium", "selenium")
	}
}

target(startSelenium: "Starts Selenium and launches a browser") {
	startSeleniumServer()
	
	def host = seleniumConfig.selenium.server.host
	def port = seleniumConfig.selenium.server.port
	def browser = seleniumConfig.selenium.browser
	def url = seleniumConfig.selenium.url
	def maximize = seleniumConfig.selenium.windowMaximize

	event "StatusUpdate", ["Starting Selenium session for $url"]
	selenium = new DefaultSelenium(host, port, browser, url)
	selenium.start()
	if (maximize) {
		selenium.windowMaximize()
	}

	def holderClass = Class.forName("grails.plugins.selenium.SeleniumTestContextHolder", true, classLoader)
	holderClass.initialise(selenium, seleniumConfig)
}

target(stopSelenium: "Stops Selenium") {
	event "StatusUpdate", ["Stopping Selenium session"]
	selenium?.stop()
	selenium = null
	stopSeleniumServer()
	def holderClass = Class.forName("grails.plugins.selenium.SeleniumTestContextHolder", true, classLoader)
	holderClass.clear()
}
