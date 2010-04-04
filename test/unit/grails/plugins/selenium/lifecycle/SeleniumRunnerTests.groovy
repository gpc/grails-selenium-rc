package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.DefaultSelenium
import grails.plugins.selenium.SeleniumTestContext
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.nullValue
import static org.hamcrest.CoreMatchers.sameInstance

@WithGMock
class SeleniumRunnerTests {

	def config
	SeleniumTestContext context
	SeleniumRunner runner

	@Before void setUp() {
		context = mock(SeleniumTestContext)
		runner = new SeleniumRunner(context)
	}

	@Test void startsAndStopsSelenium() {
		def config = new ConfigSlurper().parse("""
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
		context.config.returns(config).stub()
		def selenium = mock(DefaultSelenium, constructor("localhost", 4444, "*firefox", "http://localhost:8080"))
		ordered {
			selenium.start()
			context.selenium.set(sameInstance(selenium))
			selenium.stop()
			context.selenium.set(nullValue())
		}

		play {
			runner.startSelenium()
			runner.stopSelenium()
		}
	}

	@Test void maximisesWindowIfRequired() {
		def config = new ConfigSlurper().parse("""
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
		context.config.returns(config).stub()
		def selenium = mock(DefaultSelenium, constructor("localhost", 4444, "*firefox", "http://localhost:8080"))
		ordered {
			selenium.start()
			selenium.windowMaximize()
			context.selenium.set(sameInstance(selenium))
		}

		play {
			runner.startSelenium()
		}
	}

}
