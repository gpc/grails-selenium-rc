package grails.plugins.selenium.events

import grails.plugins.selenium.SeleniumTestContext
import org.codehaus.groovy.grails.plugins.PluginManagerHolder

class SeleniumServerRunner implements EventHandler {

	private final SeleniumTestContext context
	private seleniumServer

	SeleniumServerRunner(SeleniumTestContext context) {
		this.context = context
	}

	def boolean handles(String event) {
		event in [EVENT_TEST_SUITE_START, EVENT_TEST_SUITE_END]
	}

	def void onEvent(String event, Object... arguments) {
		def testPhase = arguments[0]
		if (testPhase =~ /selenium/) {
			switch (event) {
				case EVENT_TEST_SUITE_START:
					startServer()
					break
				case EVENT_TEST_SUITE_END:
					stopServer()
					break
			}
		}
	}

	private void startServer() {
		// The Selenium server needs to be loaded into a clean class
		// loader because the "selenium-server.jar" includes its own
		// dependencies which conflict with some of the Grails ones.
		def serverClassLoader = new URLClassLoader(
				[serverJar.toURI().toURL()] as URL[],
				ClassLoader.systemClassLoader)

		// HACK - It seems that Jetty will load classes using the thread
		// context class loader, so we temporarily make the server class#
		// loader the context CL.
		def oldContextCL = Thread.currentThread().contextClassLoader
		Thread.currentThread().contextClassLoader = serverClassLoader

		// Create a configuration and start the server.
		def conf = serverClassLoader.loadClass("org.openqa.selenium.server.RemoteControlConfiguration").newInstance()
		conf.port = context.config.selenium.server.port
		// some nasty browser specific forced config
		switch (context.config.selenium.browser) {
			case ~/^\*safari/:
				if (!context.config.selenium.singleWindow) log.warn "selenium.singleWindow=false is not supported in Safari"
				conf.singleWindow = true
				break
			case ~/^\*iexplore/:
				if (context.config.selenium.singleWindow) log.warn "selenium.singleWindow=true is not supported in Internet Explorer"
				conf.singleWindow = false
				break
			default:
				conf.singleWindow = context.config.selenium.singleWindow
		}

		seleniumServer = serverClassLoader.loadClass("org.openqa.selenium.server.SeleniumServer").newInstance(context.config.selenium.slow, conf)
		seleniumServer.start()

		// Jetty is now listening on separate threads, so it appears
		// to be safe to reset the context class loader for this thread.
		// That's the theory anyway.
		Thread.currentThread().contextClassLoader = oldContextCL
	}

	private void stopServer() {
		seleniumServer?.stop()
	}

	private File getServerJar() {
		new File("lib/server/selenium-server.jar") // TODO: inject this
	}

}
