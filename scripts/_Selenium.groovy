import org.codehaus.groovy.grails.commons.ConfigurationHolder

seleniumManager = null

target(seleniumInit: "Initialises Selenium manaager") {
	def managerClass = classLoader.loadClass("grails.plugins.selenium.SeleniumManager")
	seleniumManager = managerClass.instance
	seleniumManager.config = seleniumConfig
}

target(startSeleniumServer: "Starts Selenium server") {
	depends(seleniumInit)
	event("StatusUpdate", ["starting selenium server"])
	seleniumManager.startServer("${seleniumRcPluginDir}/lib/server/selenium-server.jar")
}

target(startSelenium: "Starts Selenium instance, launching a browser window") {
	depends(configureServerContextPath, createConfig)
	def url
	if (seleniumManager.config.selenium.url) {
		url = seleniumManager.config.selenium.url
		event("StatusUpdate", ["using url $url from selenium config"])
	} else if (config.grails.serverURL) {
		url = config.grails.serverURL
		event("StatusUpdate", ["using url $url from config.grails.serverURL"])
	} else {
		def host = serverHost ?: "localhost"
		def port = System.properties."server.port" ?: serverPort
		def path = ConfigurationHolder.config.grails.app.context ?: serverContextPath
		url = "http://$host:${port}$path"
		event("StatusUpdate", ["using url $url"])
	}
	if (!url.endsWith("/")) url = "$url/"
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