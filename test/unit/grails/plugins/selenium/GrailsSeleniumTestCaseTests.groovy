package grails.plugins.selenium

import com.thoughtworks.selenium.DefaultSelenium
import com.thoughtworks.selenium.SeleneseTestBase
import grails.test.GrailsUnitTestCase
import junit.framework.AssertionFailedError
import junit.framework.ComparisonFailure
import org.gmock.WithGMock
import com.thoughtworks.selenium.Selenium
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*
import org.junit.After

@WithGMock
class GrailsSeleniumTestCaseTests extends GrailsUnitTestCase {

	GrailsSeleniumTestCase testCase = new GrailsSeleniumTestCase()
	Selenium selenium = new DefaultSelenium(null)

	@Before
	void setUp() {
		super.setUp()
		testCase.name = "testSomething"
		SeleniumManager.instance.selenium = selenium
	}

	@After
	void tearDown() {
		super.tearDown()
		SeleniumManager.instance.selenium = null
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
		mock(selenium).isTextPresent("some string").returns(true)
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
		mock(selenium).isAlertPresent().returns(true)
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
		mock(selenium).getText("id=foo").returns("expected value")
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
		mock(selenium).getLocation().returns("http://localhost:8080/foo")
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
		mock(selenium).isTextPresent("some string").returns(true)
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
		mock(selenium).isTextPresent("some string").returns(true)
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
		mock(selenium).getText("id=foo").returns("expected value")
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
		mock(selenium).getText("id=foo").returns("expected value")
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
		mock(selenium).getText("id=foo").returns("not what I expected")
		play {
			shouldFail(AssertionError) {
				testCase.assertText("expected value", "id=foo")
			}
		}
	}

	@Test
	void verifyFailsCorrectly() {
		mock(selenium).getText("id=foo").returns("not what I expected")
		play {
			testCase.verifyText("expected value", "id=foo")
			// TODO: SeleneseTestBase throws the wrong error type :(
			shouldFail(AssertionError) {
				testCase.checkForVerificationErrors()
			}
		}
	}

	@Test
	void waitForFailsCorrectly() {
		mock(selenium).getText("id=foo").returns("not what I expected")
		play {
			shouldFail(AssertionFailedError) {
				testCase.waitForText("expected value", "id=foo")
			}
		}
	}

	@Test
	void equalityAssertUsesSeleniumVersionOfAssertEquals() {
		mock(selenium).getText("id=foo").returns("expected value")
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

	@Test
	void failsCleanlyWhenWrongArgumentTypesPassedToDelegatedSeleniumMethod() {
		shouldFail(MissingMethodException) {
			testCase.assertText("expected", 3)
		}
	}

	@Test
	void booleanDynamicAssertCanBeNegated() {
		mock(selenium).isTextPresent("some string").returns(false)
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
		mock(selenium).isTextPresent("some string").returns(false)
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
		mock(selenium).isTextPresent("some string").returns(false)
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
		mock(selenium).getText("id=foo").returns("expected value")
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
		mock(selenium).getText("id=foo").returns("expected value")
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
		mock(selenium).getText("id=foo").returns("expected value")
		play {
			try {
				testCase.waitForNotText("not expected", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to waitForNotText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

}