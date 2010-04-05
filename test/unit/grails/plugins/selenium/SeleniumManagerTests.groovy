package grails.plugins.selenium

import com.thoughtworks.selenium.DefaultSelenium
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_SUITE_END
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_SUITE_START
import grails.plugins.selenium.lifecycle.SeleniumServerRunner

@WithGMock
class SeleniumManagerTests {

	SeleniumManager manager

	@Before void setUp() {
		manager = new SeleniumManager()
		manager.seleniumServerRunner = mock(SeleniumServerRunner)
	}

	@Test void startsAndStopsSeleniumAndServerInCorrectSequence() {
		manager.config = new ConfigSlurper().parse("""
			selenium {
				server {
					host = "localhost"
					port = 4444
				}
				browser = "*firefox"
				url = "http://localhost:8080"
				windowMaximize = false
			}
		""")
		def selenium = mock(DefaultSelenium, constructor("localhost", 4444, "*firefox", "http://localhost:8080"))
		ordered {
			manager.seleniumServerRunner.startServer()
			selenium.start()
			selenium.stop()
			manager.seleniumServerRunner.stopServer()
		}

		play {
			manager.receiveGrailsBuildEvent EVENT_TEST_SUITE_START, "selenium"
			manager.receiveGrailsBuildEvent EVENT_TEST_SUITE_END, "selenium"
		}
	}

	@Test void maximisesWindowIfRequired() {
		manager.config = new ConfigSlurper().parse("""
			selenium {
				server {
					host = "localhost"
					port = 4444
				}
				browser = "*firefox"
				url = "http://localhost:8080"
				windowMaximize = true
			}
		""")
		def selenium = mock(DefaultSelenium, constructor("localhost", 4444, "*firefox", "http://localhost:8080"))
		ordered {
			manager.seleniumServerRunner.startServer()
			selenium.start()
			selenium.windowMaximize()
		}

		play {
			manager.receiveGrailsBuildEvent EVENT_TEST_SUITE_START, "selenium"
		}
	}

}
