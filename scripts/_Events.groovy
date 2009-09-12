import org.openqa.selenium.server.SeleniumServer

eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
}

def seleniumServer

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		event("StatusUpdate", ["starting selenium server"])
		seleniumServer = new SeleniumServer()
		seleniumServer.start()
	}
}

eventTestSuiteEnd = {String type, testSuite ->
	if (type == "selenium") {
		event("StatusUpdate", ["stopping selenium server"])
		seleniumServer?.stop()
	}
}
