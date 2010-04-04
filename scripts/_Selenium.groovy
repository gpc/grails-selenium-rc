seleniumManager = null

target(seleniumInit: "Initialises Selenium manaager") {
	event "StatusUpdate", ["Initialising Selenium Manager"]
	def managerClass = classLoader.loadClass("grails.plugins.selenium.SeleniumManager")
	seleniumManager = managerClass.instance
	seleniumManager.config = seleniumConfig
	eventListener.addGrailsBuildListener(seleniumManager)
}

target(startSeleniumServer: "Starts Selenium server") {
	depends(seleniumInit)
	event "StatusUpdate", ["starting selenium server"]
	seleniumManager.startServer("${seleniumRcPluginDir}/lib/server/selenium-server.jar")
}

target(startSelenium: "Starts Selenium instance, launching a browser window") {
	depends(configureServerContextPath, createConfig)
	event "StatusUpdate", ["starting selenium instance"]
	seleniumManager.startSelenium()
}

target(stopSelenium: "Stops Selenium instance, closing the browser window") {
	event "StatusUpdate", ["stopping selenium instance"]
	seleniumManager.stopSelenium()
}

target(stopSeleniumServer: "Stops Selenium server") {
	event "StatusUpdate", ["stopping selenium server"]
	seleniumManager.stopServer()
}