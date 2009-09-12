eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
}

static final SELENIUM_HOLDER_CLASS = "com.energizedwork.grails.plugins.seleniumrc.SeleniumHolder"
def seleniumServer

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		def seleniumConfig = [:]
		seleniumConfig.host = "localhost"
		seleniumConfig.port = 4444
		seleniumConfig.browser = "*safari"
		seleniumConfig.browserUrl = config.grails.serverURL
		seleniumConfig.slowResources = false
		event("StatusUpdate", ["selenium config: $seleniumConfig"])

		event("StatusUpdate", ["starting selenium server"])
		def conf = new org.openqa.selenium.server.RemoteControlConfiguration()
		conf.port = seleniumConfig.port
		conf.singleWindow = true
		seleniumServer = new org.openqa.selenium.server.SeleniumServer(seleniumConfig.slowResources, conf)
		seleniumServer.start()

		event("StatusUpdate", ["starting selenium instance"])

		com.energizedwork.grails.plugins.seleniumrc.SeleniumHolder.selenium = new com.thoughtworks.selenium.GroovySelenium(new com.thoughtworks.selenium.DefaultSelenium(seleniumConfig.host, seleniumConfig.port, seleniumConfig.browser, seleniumConfig.browserUrl))
		com.energizedwork.grails.plugins.seleniumrc.SeleniumHolder.selenium.start()
	}
}

eventTestSuiteEnd = {String type, testSuite ->
	if (type == "selenium") {
		event("StatusUpdate", ["stopping selenium instance"])
		com.energizedwork.grails.plugins.seleniumrc.SeleniumHolder.selenium?.stop()
		com.energizedwork.grails.plugins.seleniumrc.SeleniumHolder.selenium = null

		event("StatusUpdate", ["stopping selenium server"])
		seleniumServer?.stop()
	}
}
