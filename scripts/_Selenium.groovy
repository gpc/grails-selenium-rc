seleniumManager = null

loadSeleniumManagerClass = { ->
	def doLoad = { -> classLoader.loadClass("grails.plugins.selenium.SeleniumManager") }
	try {
		doLoad()
	} catch (ClassNotFoundException e) {
		event("StatusUpdate", ["Fucksocks SeleniumManager isn't compiled yet"])
		includeTargets << grailsScript("_GrailsCompile") 
		compile()
		doLoad()
	}  
}

target(seleniumInit: "Initialises Selenium manaager") {
	def managerClass = loadSeleniumManagerClass()
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
	depends(configureServerContextPath)
	def url = seleniumManager.config.selenium.url ?: "http://${serverHost ?: 'localhost'}:${serverPort}$serverContextPath/"
	event("StatusUpdate", ["starting selenium instance for $url"])
	seleniumManager.startSelenium(url)
}

target(stopSelenium: "Stops Selenium instance, closing the browser window") {
	event("StatusUpdate", ["stopping selenium instance"])
	seleniumManager.stopSelenium()
}

target(stopSeleniumServer: "Stops Selenium server") {
	event("StatusUpdate", ["stopping selenium server"])
	seleniumManager.stopServer()
}