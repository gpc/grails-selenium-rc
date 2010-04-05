package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.SeleniumTestContext
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

@WithGMock
class DefaultSeleniumServerRunnerTests {

	SeleniumTestContext context
	DefaultSeleniumServerRunner runner

	@Before
	void setUp() {
		def config = new ConfigSlurper().parse("""
selenium {
	server {
		host = "localhost"
		port = 4444
	}
	browser = "*firefox"
	defaultTimeout = 60000
	defaultInterval = 250
	slow = false
	singleWindow = true
	windowMaximize = false
	screenshot {
		dir = "target/test-reports/screenshots"
		onFail = false
	}
}
		""")

		context = mock(SeleniumTestContext)
		context.config.returns(config).stub()

		runner = new DefaultSeleniumServerRunner(context)
	}

	@Test
	void startsAndStopsServer() {
		play {
			runner.startServer()
			assertTrue "Selenium server should be running", isServerRunning()
			runner.stopServer()
			assertFalse "Selenium server should not be running", isServerRunning()
		}
	}

	@Test void serverJarExists() {
		def jar = runner.serverJar
		assertTrue "Selenium server jar $jar.canonicalPath is not found", jar.isFile()
	}

	private boolean isServerRunning() {
		def socket = null
		try {
			socket = new Socket("localhost", 4444)
			return socket.isConnected()
		} catch (ConnectException e) {
			return false
		} finally {
			socket?.close()
		}
	}

}
