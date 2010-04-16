package grails.plugins.selenium.test.support

import org.junit.Test
import org.junit.Before
import static org.junit.Assert.*
import org.gmock.WithGMock
import org.codehaus.groovy.grails.test.GrailsTestType

@WithGMock
class SeleniumGrailsTestTypeTests {

	GrailsTestType delegateTestType
	SeleniumGrailsTestType seleniumTestType

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

		delegateTestType = mock(GrailsTestType)
		seleniumTestType = new SeleniumGrailsTestType(delegateTestType, config)
	}

	@Test
	void startsSeleniumThenRunsTestsThenStopsSelenium() {
		delegateTestType.prepare(anything(), anything(), anything()).returns(1)
		delegateTestType.cleanup()

		play {
			assertFalse "Selenium server is already running", isServerRunning()
			seleniumTestType.prepare(null, null, null)
			assertTrue "Selenium server has not started", isServerRunning()
			seleniumTestType.cleanup()
			assertFalse "Selenium server has not stopped", isServerRunning()
		}
	}

	@Test
	void doesNotStartSeleniumWhenThereAreNoTestsToRun() {
		delegateTestType.prepare(anything(), anything(), anything()).returns(0)
		delegateTestType.cleanup()

		play {
			assertFalse "Selenium server is already running", isServerRunning()
			seleniumTestType.prepare(null, null, null)
			assertFalse "Selenium server has been started", isServerRunning()
			seleniumTestType.cleanup()
			assertFalse "Selenium server has not stopped", isServerRunning()
		}
	}

	@Test
	void serverJarExists() {
		def jar = seleniumTestType.serverJar
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
