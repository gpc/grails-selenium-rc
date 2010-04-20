package grails.plugins.selenium.test.support

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.DefaultSeleniumTestContext
import grails.plugins.selenium.SeleniumTestContextHolder
import grails.plugins.selenium.lifecycle.ScreenshotGrabber
import grails.plugins.selenium.lifecycle.TestContextNotifier
import org.codehaus.groovy.grails.cli.support.GrailsBuildEventListener
import org.codehaus.groovy.grails.plugins.GrailsPluginUtils
import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
import org.codehaus.groovy.grails.test.GrailsTestType
import org.slf4j.LoggerFactory

class SeleniumGrailsTestType extends GrailsTestTypeDecorator {

	private final log = LoggerFactory.getLogger(SeleniumGrailsTestType)

	private seleniumServer
	private Selenium selenium
	private ConfigObject config

	SeleniumGrailsTestType(GrailsTestType delegate, ConfigObject config) {
		super(delegate)
		this.config = config
	}

	int prepare(GrailsTestTargetPattern[] testTargetPatterns, File compiledClassesDir, Binding buildBinding) {
		int testCount = super.prepare(testTargetPatterns, compiledClassesDir, buildBinding)
		if (testCount > 0) {
			startServer()
			startSelenium()
			SeleniumTestContextHolder.context = new DefaultSeleniumTestContext(selenium, config)
			registerBuildEventListeners(buildBinding)
		}
		return testCount
	}

	void cleanup() {
		SeleniumTestContextHolder.context = null
		stopSelenium()
		stopServer()
		super.cleanup()
	}

	private void registerBuildEventListeners(Binding buildBinding) {
		assert buildBinding.variables.containsKey("eventListener"), "eventListener is not present in the build binding"
		GrailsBuildEventListener eventListener = buildBinding.eventListener
		eventListener.addGrailsBuildListener(new TestContextNotifier())
		eventListener.addGrailsBuildListener(new ScreenshotGrabber())
	}

	private void startSelenium() {
		def host = config.selenium.server.host
		def port = config.selenium.server.port
		def browser = config.selenium.browser
		def url = config.selenium.url
		def maximize = config.selenium.windowMaximize

		selenium = new DefaultSelenium(host, port, browser, url)
		selenium.start()
		if (maximize) {
			selenium.windowMaximize()
		}
	}

	private void stopSelenium() {
		selenium?.stop()
		selenium = null
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

		// Create a configuration
		def conf = serverClassLoader.loadClass("org.openqa.selenium.server.RemoteControlConfiguration").newInstance()
		initRemoteControlConfiguration(conf)

		// Load the server class and start the server
		seleniumServer = serverClassLoader.loadClass("org.openqa.selenium.server.SeleniumServer").newInstance(runInSlowMode(), conf)
		seleniumServer.start()

		// Jetty is now listening on separate threads, so it appears
		// to be safe to reset the context class loader for this thread.
		// That's the theory anyway.
		Thread.currentThread().contextClassLoader = oldContextCL
	}

	private void initRemoteControlConfiguration(conf) {
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
	}

	private void stopServer() {
		seleniumServer?.stop()
	}

	private File getServerJar() {
		def pluginDirectory = GrailsPluginUtils.getPluginDirForName("selenium-rc").file
		new File(pluginDirectory, "lib/server/selenium-server.jar") // TODO: inject or scan for jar name
	}

	private boolean runInSlowMode() {
		return config.selenium.slow ?: false
	}
}
