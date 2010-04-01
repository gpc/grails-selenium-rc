package grails.plugins.selenium.events

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumTestContext
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_SUITE_END
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_SUITE_START
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

@WithGMock
class SeleniumServerRunnerTests {

	Selenium selenium
	SeleniumTestContext context
	SeleniumServerRunner runner

	@Before
	void setUp() {
		selenium = mock(Selenium)

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
		context.selenium.returns(selenium).stub()

		runner = new SeleniumServerRunner(context)
	}

	@Test
	void doesNotStartServerIfNotRunningSeleniumSuite() {
		play {
			runner.onEvent(EVENT_TEST_SUITE_START, "unit")
			assertFalse "Selenium server should not be running", isServerRunning()
			runner.onEvent(EVENT_TEST_SUITE_END, "unit")
			assertFalse "Selenium server should not be running", isServerRunning()
		}
	}

	@Test
	void startsAndStopsServer() {
		play {
			runner.onEvent(EVENT_TEST_SUITE_START, "selenium")
			assertTrue "Selenium server should be running", isServerRunning()
			runner.onEvent(EVENT_TEST_SUITE_END, "selenium")
			assertFalse "Selenium server should not be running", isServerRunning()
		}
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
