package grails.plugins.selenium.events

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumTestContext
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.EventHandler.*

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
			runner.onEvent(EVENT_TEST_SUITE_END, "unit")
		}
	}

	@Test void startsAndStopsServer() {
		play {
			runner.onEvent(EVENT_TEST_SUITE_START, "selenium")
			runner.onEvent(EVENT_TEST_SUITE_END, "selenium")
		}
	}

}
