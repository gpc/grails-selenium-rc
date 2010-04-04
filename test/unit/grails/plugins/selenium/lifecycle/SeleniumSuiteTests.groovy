package grails.plugins.selenium.lifecycle

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_SUITE_END
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_SUITE_START

@WithGMock
class SeleniumSuiteTests {

	private SeleniumSuite suite = new SeleniumSuite(null)

	@Before
	void setUp() {
		suite.seleniumServerRunner = mock(SeleniumServerRunner)
		suite.seleniumRunner = mock(SeleniumRunner)
	}

	@Test
	void startsAndStopsServerAndSelenium() {
		ordered {
			suite.seleniumServerRunner.startServer()
			suite.seleniumRunner.startSelenium()
			suite.seleniumRunner.stopSelenium()
			suite.seleniumServerRunner.stopServer()
		}
		play {
			suite.onEvent(EVENT_TEST_SUITE_START, "selenium")
			suite.onEvent(EVENT_TEST_SUITE_END, "selenium")
		}
	}

	@Test
	void ignoresOtherTestTypes() {
		play {
			suite.onEvent(EVENT_TEST_SUITE_START, "unit")
			suite.onEvent(EVENT_TEST_SUITE_END, "unit")
		}
	}

}
