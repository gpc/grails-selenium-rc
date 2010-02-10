package grails.plugins.selenium

import org.slf4j.LoggerFactory

@Singleton class SeleniumManager {

	private static final log = LoggerFactory.getLogger(SeleniumManager)

	private def seleniumServer
	ConfigObject config
	GrailsSelenium selenium
	
	void startServer(serverJar) {
		// The Selenium server needs to be loaded into a clean class
		// loader because the "selenium-server.jar" includes its own
		// dependencies which conflict with some of the Grails ones.
		def serverClassLoader = new URLClassLoader(
				[new File(serverJar).toURI().toURL()] as URL[],
				ClassLoader.systemClassLoader)

		// HACK - It seems that Jetty will load classes using the thread
		// context class loader, so we temporarily make the server class#
		// loader the context CL.
		def oldContextCL = Thread.currentThread().contextClassLoader
		Thread.currentThread().contextClassLoader = serverClassLoader

		// Create a configuration and start the server.
		def conf = serverClassLoader.loadClass("org.openqa.selenium.server.RemoteControlConfiguration").newInstance()
		conf.port = config.selenium.server.port
		// some nasty browser specific forced config
		switch (config.selenium.browser) {
			case ~/^\*safari/:
				if (!config.selenium.singleWindow) log.warn "selenium.singleWindow=false is not supported in Safari"
				conf.singleWindow = true
				break
			case ~/^\*iexplore/:
				if (config.selenium.singleWindow) log.warn "selenium.singleWindow=true is not supported in Internet Explorer"
				conf.singleWindow = false
				break
			default:
				conf.singleWindow = config.selenium.singleWindow
		}

		seleniumServer = serverClassLoader.loadClass("org.openqa.selenium.server.SeleniumServer").newInstance(config.selenium.slow, conf)
		seleniumServer.start()

		// Jetty is now listening on separate threads, so it appears
		// to be safe to reset the context class loader for this thread.
		// That's the theory anyway.
		Thread.currentThread().contextClassLoader = oldContextCL
	}

	void stopServer() {
		seleniumServer?.stop()
	}

	void startSelenium(serverURL) {
		def host = config.selenium.server.host
		def port = config.selenium.server.port
		def browser = config.selenium.browser
		def url = config.selenium.url ?: serverURL
		selenium = new GrailsSelenium(host, port, browser, url)
		selenium.defaultTimeout = config.selenium.defaultTimeout
//		selenium.screenshotDir = new File(config.selenium.screenshotDir)

		selenium.start()
		if (config.selenium.windowMaximize) {
			selenium.windowMaximize()
		}
	}

	void stopSelenium() {
		selenium?.stop()
		selenium = null
	}

}