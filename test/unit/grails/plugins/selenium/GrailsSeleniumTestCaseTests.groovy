package grails.plugins.selenium

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.SeleneseTestBase
import com.thoughtworks.selenium.Selenium
import grails.test.GrailsUnitTestCase
import junit.framework.AssertionFailedError
import junit.framework.ComparisonFailure
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import com.thoughtworks.selenium.Wait.WaitTimedOutException

@WithGMock
class GrailsSeleniumTestCaseTests extends GrailsUnitTestCase {

	GrailsSeleniumTestCase testCase = new GrailsSeleniumTestCase()
	Selenium mockSelenium

	@Before
	void setUp() {
		super.setUp()

		testCase.name = "testSomething"

		mockSelenium = mock(Selenium)
		SeleniumTestContextHolder.context = mock(SeleniumTestContext)
		SeleniumTestContextHolder.context.getSelenium().returns(mockSelenium).stub()
		SeleniumTestContextHolder.context.getTimeout().returns(500).stub()
	}

	@After
	void tearDown() {
		super.tearDown()
		SeleniumTestContextHolder.context = null
	}

	@Test
	void rootUrlIsBasedOnConfig() {
		mockConfig "app.context = '/foo'"
		assertThat testCase.contextPath, equalTo("/foo")
	}

	@Test
	void doesNotDelegateToSeleneseTestBaseWhenGroovyTestCaseHasSameMethod() {
		testCase.@base = mock(SeleneseTestBase)
		play {
			// non-static method that exists on both GroovyTestCase and SeleneseTestBase
			testCase.setUp()
			// static method that exists on both GroovyTestCase and SeleneseTestBase
			testCase.assertEquals "foo", "foo"
		}
	}

	@Test
	void methodsDelegateToSeleneseTestBase() {
		try {
			testCase.verifyTrue(true)
		} catch (MissingMethodException e) {
			fail "Call to verifyTrue was not delegated to SeleneseTestBase"
		}
	}

	@Test
	void dynamicBooleanAssertWithOneArgDelegatedToSelenium() {
		mockSelenium.isTextPresent("some string").returns(true)
		play {
			try {
				testCase.assertTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to assertTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	@Test
	void booleanAssertWithNoArgsDelegatedToSelenium() {
		mockSelenium.isAlertPresent().returns(true)
		play {
			try {
				testCase.assertAlertPresent()
			} catch (MissingMethodException e) {
				fail "Call to assertAlertPresent was not delegated to Selenium.isAlertPresent: $e.message"
			}
		}
	}

	@Test
	void equalityAssertWithOneArgDelegatedToSelenium() {
		mockSelenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.assertText("expected value", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to assertText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

	@Test
	void equalityAssertWithNoArgsDelegatedToSelenium() {
		mockSelenium.getLocation().returns("http://localhost:8080/foo")
		play {
			try {
				testCase.assertLocation("http://localhost:8080/foo")
			} catch (MissingMethodException e) {
				fail "Call to assertLocation was not delegated to Selenium.getLocation: $e.message"
			}
		}
	}

	@Test
	void booleanVerifyDelegatesToSelenium() {
		mockSelenium.isTextPresent("some string").returns(true)
		play {
			try {
				testCase.verifyTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	@Test
	void booleanWaitForDelegatesToSelenium() {
		mockSelenium.isTextPresent("some string").returns(true)
		play {
			try {
				testCase.waitForTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	@Test
	void equalityVerifyDelegatesToSelenium() {
		mockSelenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.verifyText("expected value", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	@Test
	void equalityWaitForDelegatesToSelenium() {
		mockSelenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.waitForText("expected value", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	@Test
	void assertFailsCorrectly() {
		mockSelenium.getText("id=foo").returns("not what I expected")
		play {
			shouldFail(AssertionError) {
				testCase.assertText("expected value", "id=foo")
			}
		}
	}

	@Test
	void verifyFailsCorrectly() {
		mockSelenium.getText("id=foo").returns("not what I expected")
		play {
			testCase.verifyText("expected value", "id=foo")
			// TODO: SeleneseTestBase throws the wrong error type :(
			shouldFail(AssertionError) {
				testCase.checkForVerificationErrors()
			}
		}
	}

	@Test(expected = WaitTimedOutException)
	void waitForFailsCorrectly() {
		mockSelenium.getText("id=foo").returns("not what I expected")
		play {
			testCase.waitForText("expected value", "id=foo")
		}
	}

	@Test
	void equalityAssertUsesSeleniumVersionOfAssertEquals() {
		mockSelenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.assertText(/regex:e[\w\s]+e/, "id=foo")
			} catch (ComparisonFailure e) {
				fail "Call to assertText was not delegated to SeleneseTextBase.assertEquals: $e.message"
			} catch (MissingMethodException e) {
				fail "Call to assertText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

	@Ignore @Test(expected = MissingMethodException)
	void failsCleanlyWhenWrongArgumentTypesPassedToDelegatedSeleniumMethod() {
		SeleniumTestContextHolder.context.selenium = new DefaultSelenium(null)
		testCase.assertText("expected", 3)
	}

	@Test
	void booleanDynamicAssertCanBeNegated() {
		mockSelenium.isTextPresent("some string").returns(false)
		play {
			try {
				testCase.assertNotTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to assertNotTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	@Test
	void booleanDynamicVerifyCanBeNegated() {
		mockSelenium.isTextPresent("some string").returns(false)
		play {
			try {
				testCase.verifyNotTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to verifyNotTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	@Test
	void booleanDynamicWaitForCanBeNegated() {
		mockSelenium.isTextPresent("some string").returns(false)
		play {
			try {
				testCase.waitForNotTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to waitForNotTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	@Test
	void equalityDynamicAssertCanBeNegated() {
		mockSelenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.assertNotText("not expected", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to assertNotText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

	@Test
	void equalityDynamicVerifyCanBeNegated() {
		mockSelenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.verifyNotText("not expected", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to verifyNotText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

	@Test
	void equalityDynamicWaitForCanBeNegated() {
		mockSelenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.waitForNotText("not expected", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to waitForNotText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

}