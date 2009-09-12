import org.openqa.selenium.server.SeleniumServer
import org.openqa.selenium.server.RemoteControlConfiguration
import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.GroovySelenium

eventAllTestsStart = {
	if (binding.variables.containsKey("functionalTests")) {
		functionalTests << "selenium"
	}
}

static final SELENIUM_HOLDER_CLASS = "com.energizedwork.grails.plugins.seleniumrc.SeleniumHolder"
def seleniumServer

eventTestSuiteStart = {String type ->
	if (type == "selenium") {
		def seleniumConfig = [:]
		seleniumConfig.host = "localhost"
		seleniumConfig.port = 4444
		seleniumConfig.browser = "*safari"
		seleniumConfig.browserUrl = config.grails.serverURL
		seleniumConfig.slowResources = true
		event("StatusUpdate", ["selenium config: $seleniumConfig"])

		event("StatusUpdate", ["starting selenium server"])
		def conf = new RemoteControlConfiguration()
		conf.port = seleniumConfig.port
		conf.singleWindow = true
		seleniumServer = new SeleniumServer(seleniumConfig.slowResources, conf)
		seleniumServer.start()

		event("StatusUpdate", ["starting selenium instance"])

		def holder = Thread.currentThread().contextClassLoader.loadClass(SELENIUM_HOLDER_CLASS)
		holder.selenium = new GroovySelenium(new DefaultSelenium(seleniumConfig.host, seleniumConfig.port, seleniumConfig.browser, seleniumConfig.browserUrl))
		holder.selenium.start()
	}
}

eventTestSuiteEnd = {String type, testSuite ->
	if (type == "selenium") {
		event("StatusUpdate", ["stopping selenium instance"])
		def holder = Thread.currentThread().contextClassLoader.loadClass(SELENIUM_HOLDER_CLASS)
		holder.selenium?.stop()
		holder.selenium = null

		event("StatusUpdate", ["stopping selenium server"])
		seleniumServer?.stop()
	}
}
