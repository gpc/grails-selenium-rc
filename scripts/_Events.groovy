def seleniumManager

eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
	functionalTestsPreparation = {
		packageApp()
		runApp()

		def managerClass = Thread.currentThread().contextClassLoader.loadClass("com.energizedwork.grails.plugins.seleniumrc.SeleniumManager")
		seleniumManager = managerClass.instance
		seleniumManager.loadConfig()

		def helperClass = Thread.currentThread().contextClassLoader.loadClass("com.energizedwork.grails.plugins.seleniumrc.GrailsSeleniumTestHelper")
		return helperClass.newInstance(grailsSettings, classLoader, resolveResources)
	}
}

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
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
