def seleniumManager

includeTargets << new File("${seleniumRcPluginDir}/scripts/_Selenium.groovy")

eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
	// support the custom phase in the spock plugin
	if (binding.variables.containsKey("functional-specTests")) {
		binding.'functional-specTests' << "selenium"
	}
}

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		startSelenium()
	}
}

eventTestSuiteEnd = {String type, suite ->
	if (type == "selenium") {
		stopSelenium()
	}
}
