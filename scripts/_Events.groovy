eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
}

def seleniumServer

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		com.energizedwork.grails.plugins.seleniumrc.SeleniumConfigurationHolder.loadSeleniumConfig()
		def seleniumConfig = com.energizedwork.grails.plugins.seleniumrc.SeleniumConfigurationHolder.config
		event("StatusUpdate", ["selenium config: $seleniumConfig"])

		event("StatusUpdate", ["starting selenium server"])
		def conf = new org.openqa.selenium.server.RemoteControlConfiguration()
		conf.port = seleniumConfig.selenium.port
		conf.singleWindow = seleniumConfig.selenium.singleWindow
		seleniumServer = new org.openqa.selenium.server.SeleniumServer(seleniumConfig.selenium.slowResources, conf)
		seleniumServer.start()

		event("StatusUpdate", ["starting selenium instance"])

		com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase.selenium = new com.thoughtworks.selenium.GroovySelenium(new com.thoughtworks.selenium.DefaultSelenium(seleniumConfig.selenium.host, seleniumConfig.selenium.port, seleniumConfig.selenium.browser, seleniumConfig.selenium.url ?: config.grails.serverURL))
		com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase.selenium.start()
	}
}

eventTestSuiteEnd = {String type, testSuite ->
	if (type == "selenium") {
		event("StatusUpdate", ["stopping selenium instance"])
		com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase.selenium?.stop()
		com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase.selenium = null

		event("StatusUpdate", ["stopping selenium server"])
		seleniumServer?.stop()
	}
}
