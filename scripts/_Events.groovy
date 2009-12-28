includeTargets << new File("$seleniumRcPluginDir/scripts/_Selenium.groovy")

eventAllTestsStart = {
	loadSeleniumConfig()
	if (seleniumManager.config.selenium.remote) {
		event("StatusUpdate", ["Running Selenium in remote mode"])
		otherTests << "selenium"
	} else {
		functionalTests << "selenium"
		// TODO: spock support is broken as of Grails 1.2 and Spock 0.4-SNAPSHOT
//		if (binding.variables.containsKey("spockPluginDir")) {
//			def specTestTypeClass = loadSpecTestTypeClass()
//  			functionalTests << specTestTypeClass.newInstance("spock", "selenium")
//		}
	}
}

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		startSelenium()
	}
}

eventTestSuiteEnd = {String type ->
	if (type == "selenium") {
		stopSelenium()
	}
}
