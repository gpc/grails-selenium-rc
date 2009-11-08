package grails.plugins.selenium

import com.thoughtworks.selenium.SeleneseTestBase
import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.GrailsSeleniumTestCase
import grails.test.GrailsUnitTestCase
import junit.framework.AssertionFailedError
import junit.framework.ComparisonFailure
import org.gmock.WithGMock

@WithGMock
class GrailsSeleniumTestCaseTests extends GrailsUnitTestCase {

	GrailsSeleniumTestCase testCase
	def selenium

	void setUp() {
		super.setUp()

		testCase = new GrailsSeleniumTestCase()
		testCase.name = "testSomething"

		selenium = new GrailsSelenium(null)
		testCase.metaClass.getSelenium = {-> selenium }
	}

	void testRootUrlIsBasedOnConfig() {
		mockConfig "app.context = '/foo'"
		assertEquals "/foo", testCase.contextPath
	}

	void testDoesNotDelegateToSeleneseTestBaseWhenGroovyTestCaseHasSameMethod() {
		testCase.@base = mock(SeleneseTestBase)
		mock(selenium).setContext("SeleniumTest.testSomething")
		play {
			// non-static method that exists on both GroovyTestCase and SeleneseTestBase
			testCase.setUp()
			// static method that exists on both GroovyTestCase and SeleneseTestBase
			testCase.assertEquals "foo", "foo"
		}
	}

	void testMethodsDelegateToSeleneseTestBase() {
		try {
			testCase.verifyTrue(true)
		} catch (MissingMethodException e) {
			fail "Call to verifyTrue was not delegated to SeleneseTestBase"
		}
	}

	void testDynamicBooleanAssertWithOneArg() {
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
				testCase.assertText("expected value", "id=foo")
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
				testCase.verifyText("expected value", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testEqualityWaitForDelegatesToSelenium() {
		mock(selenium).getText("id=foo").returns("expected value")
		play {
			try {
				testCase.waitForText("expected value", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testAssertFailsCorrectly() {
		mock(selenium).getText("id=foo").returns("not what I expected")
		play {
			shouldFail(AssertionError) {
				testCase.assertText("expected value", "id=foo")
			}
		}
	}

	void testVerifyFailsCorrectly() {
		mock(selenium).getText("id=foo").returns("not what I expected")
		play {
			testCase.verifyText("expected value", "id=foo")
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
				testCase.waitForText("expected value", "id=foo")
			}
		}
	}

	void testEqualityAssertUsesSeleniumVersionOfAssertEquals() {
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

	void testFailsCleanlyWhenWrongArgumentTypesPassedToDelegatedSeleniumMethod() {
		shouldFail(MissingMethodException) {
			testCase.assertText("expected", 3)
		}
	}

	void testBooleanDynamicAssertCanBeNegated() {
		mock(selenium).isTextPresent("some string").returns(false)
		play {
			try {
				testCase.assertNotTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to assertNotTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testBooleanDynamicVerifyCanBeNegated() {
		mock(selenium).isTextPresent("some string").returns(false)
		play {
			try {
				testCase.verifyNotTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to verifyNotTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testBooleanDynamicWaitForCanBeNegated() {
		mock(selenium).isTextPresent("some string").returns(false)
		play {
			try {
				testCase.waitForNotTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to waitForNotTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testEqualityDynamicAssertCanBeNegated() {
		mock(selenium).getText("id=foo").returns("expected value")
		play {
			try {
				testCase.assertNotText("not expected", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to assertNotText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

	void testEqualityDynamicVerifyCanBeNegated() {
		mock(selenium).getText("id=foo").returns("expected value")
		play {
			try {
				testCase.verifyNotText("not expected", "id=foo")
			} catch (MissingMethodException e) {
				fail "Call to verifyNotText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

	void testEqualityDynamicWaitForCanBeNegated() {
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