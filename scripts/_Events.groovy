import java.net.URLClassLoader

eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
}

def seleniumServer

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		com.energizedwork.grails.plugins.seleniumrc.SeleniumConfigurationHolder.loadSeleniumConfig()
		def seleniumConfig = com.energizedwork.grails.plugins.seleniumrc.SeleniumConfigurationHolder.config
		event("StatusUpdate", ["selenium config: $seleniumConfig"])

		event("StatusUpdate", ["starting selenium server"])
		
		// The Selenium server needs to be loaded into a clean class
		// loader because the "selenium-server.jar" includes its own
		// dependencies which conflict with some of the Grails ones.
		def serverClassLoader = new URLClassLoader(
				[ new File("${seleniumRcPluginDir}/lib/server/selenium-server.jar").toURI().toURL() ] as URL[],
			    ClassLoader.systemClassLoader)

		// HACK - It seems that Jetty will load classes using the thread
		// context class loader, so we temporarily make the server class#
		// loader the context CL.
		def oldContextCL = Thread.currentThread().contextClassLoader
		Thread.currentThread().contextClassLoader = serverClassLoader

		// Create a configuration and start the server.
		def conf = serverClassLoader.loadClass("org.openqa.selenium.server.RemoteControlConfiguration").newInstance()
		conf.port = seleniumConfig.selenium.port
		conf.singleWindow = seleniumConfig.selenium.singleWindow
		seleniumServer = serverClassLoader.loadClass("org.openqa.selenium.server.SeleniumServer").newInstance(seleniumConfig.selenium.slowResources, conf)
		seleniumServer.start()

		event("StatusUpdate", ["starting selenium instance"])
		
		// Jetty is now listening on separate threads, so it appears
		// to be safe to reset the context class loader for this thread.
		// That's the theory anyway.
		Thread.currentThread().contextClassLoader = oldContextCL

		com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase.selenium = new com.thoughtworks.selenium.GroovySelenium(new com.thoughtworks.selenium.DefaultSelenium(seleniumConfig.selenium.host, seleniumConfig.selenium.port, seleniumConfig.selenium.browser, seleniumConfig.selenium.url ?: config.grails.serverURL))
		com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase.selenium.start()
	}
}

eventTestSuiteEnd = {String type, testSuite ->
	if (type == "selenium") {
		event("StatusUpdate", ["stopping selenium instance"])
		com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase.selenium?.stop()
		com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase.selenium = null

		event("StatusUpdate", ["stopping selenium server"])
		seleniumServer?.stop()
	}
}
