eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
}

def seleniumManager

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		def clazz = Thread.currentThread().contextClassLoader.loadClass("com.energizedwork.grails.plugins.seleniumrc.SeleniumManager")
		seleniumManager = clazz.instance
		seleniumManager.loadConfig()
		event("StatusUpdate", ["starting selenium server"])
		seleniumManager.startServer("${seleniumRcPluginDir}/lib/server/selenium-server.jar")
		event("StatusUpdate", ["starting selenium instance"])
		seleniumManager.startSelenium(config.grails.serverURL)
	}
}

eventTestSuiteEnd = {String type, testSuite ->
	if (type == "selenium") {
		event("StatusUpdate", ["stopping selenium instance"])
		seleniumManager.stopSelenium()
		event("StatusUpdate", ["stopping selenium server"])
		seleniumManager.stopServer()
	}
}
