package grails.plugins.selenium.meta

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.Wait.WaitTimedOutException
import grails.plugins.selenium.SeleniumTestContext
import grails.plugins.selenium.SeleniumTestContextHolder
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test

@WithGMock
class SeleniumDynamicMethodsTests extends GrailsUnitTestCase {

	@Before
	void setUp() {
		super.setUp()
		registerMetaClass Selenium
		SeleniumDynamicMethods.enhanceSelenium()
		SeleniumTestContextHolder.context = mock(SeleniumTestContext) {
			getTimeout().returns(500).stub()
			getInterval().returns(50).stub()
		}
	}

	@After
	void tearDown() {
		super.tearDown()
		SeleniumTestContextHolder.context = null
	}

	@Test
	void canAppendAndWaitToSeleniumMethods() {
		def selenium = new DefaultSelenium(null)
		mock(selenium) {
			click "whatever"
			waitForPageToLoad "500"
		}

		play {
			selenium.clickAndWait("whatever")
		}
	}

	@Test
	void canWaitForSeleniumMethodThatReturnsBoolean() {
		def selenium = new DefaultSelenium(null)
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
		def selenium = new DefaultSelenium(null)
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
		def selenium = new DefaultSelenium(null)
		mock(selenium) {
			isTextPresent("whatever").returns(false).stub()
		}

		play {
			selenium.waitForTextPresent("whatever")
		}
	}

	@Test(expected = MissingMethodException)
	void waitForFailsForInvalidSeleniumMethod() {
		def selenium = new DefaultSelenium(null)
		selenium.waitForBlahBlahBlah("foo")
	}

}
