import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.HttpCommandProcessor

includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumConfig.groovy")
includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumServer.groovy")

selenium = null

target(registerSeleniumTestType: "Registers the selenium test type with the appropriate test phase") {
	depends(loadSeleniumConfig)

	def phase = "functional"
	if (testOptions.remote || seleniumConfig.selenium.remote) {
		event "StatusUpdate", ["Running Selenium in remote mode"]
		// override the functional test phase prep so it does not start the app
		// TODO: this is a little crude but we need some setup to be present but without starting the app
		functionalTestPhasePreparation = integrationTestPhasePreparation
		functionalTestPhaseCleanUp = integrationTestPhaseCleanUp
	}

	event "StatusUpdate", ["Selenium tests will run in the ${phase} phase"]
	binding."${phase}Tests" << "selenium"

	if (binding.variables.containsKey("spockPluginDir")) {
		def specTestTypeClass = loadSpecTestTypeClass()
		binding."${phase}Tests" << specTestTypeClass.newInstance("spock-selenium", "selenium")
	}
}

target(startSelenium: "Starts Selenium and launches a browser") {
	startSeleniumServer()

	// this isn't done in initial config construction as it requires app config to be loaded and use of -clean can cause problems
	depends(determineSeleniumUrl)
	
	def host = seleniumConfig.selenium.server.host
	def port = seleniumConfig.selenium.server.port
	def browser = seleniumConfig.selenium.browser
	def url = seleniumConfig.selenium.url
	def maximize = seleniumConfig.selenium.windowMaximize

	event "StatusUpdate", ["Starting Selenium session for $url"]
	// TODO: pull all this into a factory method
	def commandProcessor = new HttpCommandProcessor(host, port, browser, url)
	selenium = Class.forName("grails.plugins.selenium.SeleniumWrapper", true, classLoader).newInstance(new DefaultSelenium(commandProcessor), commandProcessor, seleniumConfig)
	selenium.start()
	if (maximize) {
		selenium.windowMaximize()
	}

	intialiseSeleniumHolder()
}

target(stopSelenium: "Stops Selenium") {
	event "StatusUpdate", ["Stopping Selenium session"]
	selenium?.stop()
	selenium = null
	stopSeleniumServer()
	clearSeleniumHolder()
}

target(registerSeleniumTestListeners: "Registers listeners for the Selenium test lifecycle") {
	eventListener.addGrailsBuildListener(Class.forName("grails.plugins.selenium.lifecycle.TestContextNotifier", true, classLoader).newInstance(selenium))
	eventListener.addGrailsBuildListener(Class.forName("grails.plugins.selenium.lifecycle.ScreenshotGrabber", true, classLoader).newInstance(selenium, seleniumConfig))
}

target(intialiseSeleniumHolder: "Makes the Selenium instance and config available to tests") {
	def holderClass = Class.forName("grails.plugins.selenium.SeleniumHolder", true, classLoader)
	holderClass.selenium = selenium
}

target(clearSeleniumHolder: "Cleans up test context at the end of the suite") {
	def holderClass = Class.forName("grails.plugins.selenium.SeleniumHolder", true, classLoader)
	holderClass.selenium = null
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

