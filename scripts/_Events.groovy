def seleniumManager

eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
}

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		def managerClass = Thread.currentThread().contextClassLoader.loadClass("grails.plugins.selenium.SeleniumManager")
		seleniumManager = managerClass.instance
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
