package grails.plugins.selenium

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait.WaitTimedOutException
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

@WithGMock
class SeleniumTestTests extends GrailsUnitTestCase {

	def testCase = new TestCaseImpl()

	@Before
	void configureSelenium() {
		def config = new ConfigSlurper().parse("""
			selenium {
				browser = "*firefox"
				defaultTimeout = 250
			}
		""")
		SeleniumManager.instance = new SeleniumManager(config: config)
	}

	@After
	void tearDown() {
		SeleniumManager.instance = null
	}

	@Test
	void seleniumInstanceIsAvailable() {
		SeleniumManager.instance.selenium = mock(Selenium) {
			open "/"
		}
		play {
			testCase.testOpenPage()
		}
	}

	@Test
	void configIsAvailable() {
		assertThat testCase.config.selenium.browser, equalTo("*firefox")
	}

	@Test
	void contextPathIsAvailable() {
		mockConfig "app.context = 'foo'"
		assertThat testCase.contextPath, equalTo("/foo")
	}

	@Test
	void rootContextPathWorks() {
		mockConfig "app.context = '/'"
		assertThat testCase.contextPath, equalTo("/")
	}

	@Test
	void waitForSuccess() {
		testCase.waitFor {
			true
		}
	}


	@Test(expected = WaitTimedOutException)
	void waitForThrowsTimeoutException() {
		testCase.waitFor {
			false
		}
	}

	@Test
	void waitForFailsWithMessage() {
		try {
			testCase.waitFor("something to happen") {
				false
			}
			fail "waitFor should have timed out"
		} catch (WaitTimedOutException e) {
			assertThat e.message, equalTo("Timed out waiting for: something to happen.")
		}
	}
}

@Mixin(SeleniumTest)
class TestCaseImpl {

	void testOpenPage() {
		selenium.open "/"
	}

}
