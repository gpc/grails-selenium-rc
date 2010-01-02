includeTargets << new File("$seleniumRcPluginDir/scripts/_Selenium.groovy")

eventCreateWarStart = { warName, stagingDir ->
	ant.delete(dir: "${stagingDir}/WEB-INF/classes/grails/plugins/selenium")
}

eventAllTestsStart = {
	loadSeleniumConfig()
	
	def phase = "functional"
	if (seleniumManager.config.selenium.remote) {
		event("StatusUpdate", ["Running Selenium in remote mode"])
		phase = "other"
	}

	binding."${phase}Tests" << "selenium"

	if (binding.variables.containsKey("spockPluginDir")) {
		def specTestTypeClass = loadSpecTestTypeClass()
		binding."${phase}Tests" << specTestTypeClass.newInstance("spock-selenium", "selenium")
	}
}

eventTestSuiteStart = {String type ->
	if (type =~ /selenium/) {
		startSeleniumServer()
		startSelenium()
	}
}

eventTestSuiteEnd = {String type ->
	if (type =~ /selenium/) {
		stopSelenium()
		stopSeleniumServer()
	}
}
