package grails.plugins.selenium.test.support

import com.thoughtworks.selenium.DefaultSelenium
import grails.plugins.selenium.SeleniumTestContextHolder
import org.codehaus.groovy.grails.test.GrailsTestType
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

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
	url = "http://localhost:8080/"
}
		""")

		delegateTestType = mock(GrailsTestType)
		seleniumTestType = new SeleniumGrailsTestType(delegateTestType, config)
	}

	@After
	void stopSelenium() {
		seleniumTestType.cleanup()
	}

	@Test
	void startsSeleniumThenRunsTestsThenStopsSelenium() {
		ordered {
			delegateTestType.prepare(anything(), anything(), anything()).returns(1)
			def selenium = mock(DefaultSelenium, constructor("localhost", 4444, "*firefox", "http://localhost:8080/"))
			selenium.start()
			selenium.stop()
			delegateTestType.cleanup()
		}

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
		ordered {
			delegateTestType.prepare(anything(), anything(), anything()).returns(0)
			mock(DefaultSelenium)
			delegateTestType.cleanup()
		}

		play {
			assertFalse "Selenium server is already running", isServerRunning()
			seleniumTestType.prepare(null, null, null)
			assertFalse "Selenium server has been started", isServerRunning()
			seleniumTestType.cleanup()
			assertFalse "Selenium server has not stopped", isServerRunning()
		}
	}

	@Test
	void initialisesTestContext() {
		delegateTestType.prepare(anything(), anything(), anything()).returns(1)
		def selenium = mock(DefaultSelenium, constructor("localhost", 4444, "*firefox", "http://localhost:8080/"))
		selenium.start()

		play {
			seleniumTestType.prepare(null, null, null)
			def context = SeleniumTestContextHolder.context
			assertThat "Selenium test context", context, not(nullValue())
			assertThat "Selenium instance", context.selenium, not(nullValue())
			assertThat "Timeout", context.timeout, equalTo(60000)
			assertThat "Interval", context.interval, equalTo(250)
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
