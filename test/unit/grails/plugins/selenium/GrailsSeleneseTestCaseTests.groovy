package grails.plugins.selenium

import grails.plugins.selenium.GrailsSeleneseTestCase
import com.thoughtworks.selenium.GroovySelenium
import grails.test.GrailsUnitTestCase
import junit.framework.AssertionFailedError
import org.gmock.WithGMock
import junit.framework.ComparisonFailure

@WithGMock
class GrailsSeleneseTestCaseTests extends GrailsUnitTestCase {

	GrailsSeleneseTestCase testCase
	def selenium

	void setUp() {
		super.setUp()

		testCase = new GrailsSeleneseTestCase()

		selenium = new GroovySelenium(null)
		testCase.metaClass.getSelenium = {-> selenium }
		testCase.@defaultTimeout = 250 // bypass delegating to selenium
	}

	void testMethodsDelegateToSeleneseTestBase() {
		try {
			testCase.verifyTrue(true)
		} catch (MissingMethodException e) {
			fail "Call to verifyTrue was not delegated to SeleneseTestBase"
		}
	}

	void testBooleanAssertWithOneArgDelegatedToSeleniumInstance() {
		mock(selenium).isTextPresent("some string").returns(true)
		play {
			try {
				testCase.assertTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to assertTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testBooleanAssertWithNoArgsDelegatedToSeleniumInstance() {
		mock(selenium).isAlertPresent().returns(true)
		play {
			try {
				testCase.assertAlertPresent()
			} catch (MissingMethodException e) {
				fail "Call to assertAlertPresent was not delegated to Selenium.isAlertPresent: $e.message"
			}
		}
	}

	void testEqualityAssertWithOneArgDelegatedToSeleniumInstance() {
		mock(selenium).getText("id=foo").returns("expected value")
		play {
			try {
				testCase.assertText("id=foo", "expected value")
			} catch (MissingMethodException e) {
				fail "Call to assertText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

	void testEqualityAssertWithNoArgsDelegatedToSeleniumInstance() {
		mock(selenium).getLocation().returns("http://localhost:8080/foo")
		play {
			try {
				testCase.assertLocation("http://localhost:8080/foo")
			} catch (MissingMethodException e) {
				fail "Call to assertLocation was not delegated to Selenium.getLocation: $e.message"
			}
		}
	}

	void testBooleanVerifyDelegatesToSelenium() {
		mock(selenium).isTextPresent("some string").returns(true)
		play {
			try {
				testCase.verifyTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testBooleanWaitForDelegatesToSelenium() {
		mock(selenium).isTextPresent("some string").returns(true)
		play {
			try {
				testCase.waitForTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testEqualityVerifyDelegatesToSelenium() {
		mock(selenium).getText("id=foo").returns("expected value")
		play {
			try {
				testCase.verifyText("id=foo", "expected value")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testEqualityWaitForDelegatesToSelenium() {
		mock(selenium).getText("id=foo").returns("expected value")
		play {
			try {
				testCase.waitForText("id=foo", "expected value")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testAssertFailsCorrectly() {
		mock(selenium).getText("id=foo").returns("not what I expected")
		play {
			shouldFail(AssertionError) {
				testCase.assertText("id=foo", "expected value")
			}
		}
	}

	void testVerifyFailsCorrectly() {
		mock(selenium).getText("id=foo").returns("not what I expected")
		play {
			testCase.verifyText("id=foo", "expected value")
			// TODO: SeleneseTestBase throws the wrong error type :(
			shouldFail(AssertionError) {
				testCase.checkForVerificationErrors()
			}
		}
	}

	void testWaitForFailsCorrectly() {
		mock(selenium).getText("id=foo").returns("not what I expected")
		play {
			shouldFail(AssertionFailedError) {
				testCase.waitForText("id=foo", "expected value")
			}
		}
	}

	void testEqualityAssertUsesSeleniumVersionOfAssertEquals() {
		mock(selenium).getText("id=foo").returns("expected value")
		play {
			try {
				testCase.assertText("id=foo", /regex:e[\w\s]+e/)
			} catch (ComparisonFailure e) {
				fail "Call to assertText was not delegated to SeleneseTextBase.assertEquals: $e.message"
			} catch (MissingMethodException e) {
				fail "Call to assertText was not delegated to Selenium.getText: $e.message"
			}
		}
	}
}