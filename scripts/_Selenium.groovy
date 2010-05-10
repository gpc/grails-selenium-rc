includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumConfig.groovy")
includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumServer.groovy")

seleniumRunner = null
selenium = null

target(registerSeleniumTestType: "Registers the selenium test type with the appropriate test phase") {
	depends(loadSeleniumConfig)

	if (testOptions.remote || seleniumConfig.selenium.remote) {
		event "StatusUpdate", ["Running Selenium in remote mode"]
		// override the functional test phase prep so it does not start the app
		// TODO: this is a little crude but we need some setup to be present but without starting the app
		functionalTestPhasePreparation = integrationTestPhasePreparation
		functionalTestPhaseCleanUp = integrationTestPhaseCleanUp
	}

	binding.functionalTests << "selenium"

	if (binding.variables.containsKey("spockPluginDir")) {
		def specTestTypeClass = loadSpecTestTypeClass()
		binding.functionalTests << specTestTypeClass.newInstance("spock-selenium", "selenium")
	}
}

target(startSelenium: "Starts Selenium and launches a browser") {
	startSeleniumServer()

	// this isn't done in initial config construction as it requires app config to be loaded and use of -clean can cause problems
	depends(determineSeleniumUrl)
	
	event "StatusUpdate", ["Starting Selenium session for $seleniumConfig.selenium.url"]
	seleniumRunner = Class.forName("grails.plugins.selenium.SeleniumRunner", true, classLoader).newInstance()
	selenium = seleniumRunner.startSelenium(seleniumConfig)
}

target(stopSelenium: "Stops Selenium") {
	event "StatusUpdate", ["Stopping Selenium session"]
	seleniumRunner.stopSelenium()
	selenium = null
	stopSeleniumServer()
}

target(registerSeleniumTestListeners: "Registers listeners for the Selenium test lifecycle") {
	eventListener.addGrailsBuildListener(Class.forName("grails.plugins.selenium.lifecycle.TestContextNotifier", true, classLoader).newInstance(selenium))
	eventListener.addGrailsBuildListener(Class.forName("grails.plugins.selenium.lifecycle.ScreenshotGrabber", true, classLoader).newInstance(selenium, seleniumConfig))
}

target(determineSeleniumUrl: "Determines URL Selenium tests will connect to") {
	if (!seleniumConfig.selenium.url) {
		depends(configureServerContextPath, createConfig)
		def url
		if (config.grails.serverURL) {
			url = config.grails.serverURL
		} else {
			def host = serverHost ?: "localhost"
			def port = serverPort
			def path = serverContextPath
			url = "http://$host:${port}$path"
		}
		if (!url.endsWith("/")) url = "$url/"
		seleniumConfig.selenium.url = url
	}
}

