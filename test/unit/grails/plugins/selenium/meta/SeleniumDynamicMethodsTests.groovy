package grails.plugins.selenium.meta

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait.WaitTimedOutException
import grails.plugins.selenium.SeleniumManager
import org.gmock.WithGMock
import org.junit.*

@WithGMock
class SeleniumDynamicMethodsTests {

	Selenium selenium = new DefaultSelenium(null)

	@Before
	void setUp() {
		SeleniumDynamicMethods.enhanceSelenium()
		SeleniumManager.instance.config = new ConfigSlurper().parse("""
			selenium {
				defaultTimeout = 500
				defaultInterval = 50
			}
		""")
	}

	@After
	void tearDown() {
		SeleniumManager.instance.config = null
	}

	@Test
	void canAppendAndWaitToSeleniumMethods() {
		mock(selenium) {
			click "whatever"
			waitForPageToLoad 500
		}

		play {
			selenium.clickAndWait("whatever")
		}
	}

	@Test
	void canWaitForSeleniumMethodThatReturnsBoolean() {
		mock(selenium) {
			isTextPresent("whatever").returns(false).times(2)
			isTextPresent("whatever").returns(true)
		}

		play {
			selenium.waitForTextPresent("whatever")
		}
	}

	@Test
	void canWaitForSeleniumMethodThatReturnsString() {
		mock(selenium) {
			getText("whatever").returns("incorrect").times(2)
			getText("whatever").returns("correct")
		}

		play {
			selenium.waitForText("whatever", "correct")
		}
	}

	@Test(expected = WaitTimedOutException)
	void waitForThrowsExceptionOnTimeout() {
		mock(selenium) {
			isTextPresent("whatever").returns(false).stub()
		}

		play {
			selenium.waitForTextPresent("whatever")
		}
	}

	@Test(expected = MissingMethodException)
	void waitForFailsForInvalidSeleniumMethod() {
		selenium.waitForBlahBlahBlah("foo")
	}

}
