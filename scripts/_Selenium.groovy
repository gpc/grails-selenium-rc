seleniumManager = null

target(seleniumInit: "Initialises Selenium manaager") {
	def managerClass = Thread.currentThread().contextClassLoader.loadClass("grails.plugins.selenium.SeleniumManager")
	seleniumManager = managerClass.instance
}

target(loadSeleniumConfig: "Loads Selenium configuration") {
	depends(seleniumInit)
	event("StatusUpdate", ["loading selenium config"])
	seleniumManager.loadConfig()
}

target(startSeleniumServer: "Starts Selenium server") {
	event("StatusUpdate", ["starting selenium server"])
	seleniumManager.startServer("${seleniumRcPluginDir}/lib/server/selenium-server.jar")
}

target(startSelenium: "Starts Selenium instance, launching a browser window") {
	depends(configureServerContextPath, startSeleniumServer)
	event("StatusUpdate", ["starting selenium instance"])
	def url = "http://${serverHost ?: 'localhost'}:${serverPort}$serverContextPath/"
	seleniumManager.startSelenium(url)
}

target(stopSelenium: "Stops Selenium instance, closing the browser window") {
	event("StatusUpdate", ["stopping selenium instance"])
	seleniumManager.stopSelenium()
	depends(stopSeleniumServer)
}

target(stopSeleniumServer: "Stops Selenium server") {
	event("StatusUpdate", ["stopping selenium server"])
	seleniumManager.stopServer()
}