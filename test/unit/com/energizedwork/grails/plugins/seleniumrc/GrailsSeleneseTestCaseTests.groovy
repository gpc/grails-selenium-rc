package com.energizedwork.grails.plugins.seleniumrc

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase
import com.thoughtworks.selenium.GroovySelenium
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock
import junit.framework.AssertionFailedError

@WithGMock
class GrailsSeleneseTestCaseTests extends GrailsUnitTestCase {

	GrailsSeleneseTestCase testCase
	def mockSelenium

	void setUp() {
		super.setUp()

		testCase = new GrailsSeleneseTestCase()
		testCase.selenium = mock(GroovySelenium)
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
		testCase.selenium.isTextPresent("some string").returns(true)
		play {
			try {
				testCase.assertTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to assertTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testBooleanAssertWithNoArgsDelegatedToSeleniumInstance() {
		testCase.selenium.isAlertPresent().returns(true)
		play {
			try {
				testCase.assertAlertPresent()
			} catch (MissingMethodException e) {
				fail "Call to assertAlertPresent was not delegated to Selenium.isAlertPresent: $e.message"
			}
		}
	}

	void testEqualityAssertWithOneArgDelegatedToSeleniumInstance() {
		testCase.selenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.assertText("id=foo", "expected value")
			} catch (MissingMethodException e) {
				fail "Call to assertText was not delegated to Selenium.getText: $e.message"
			}
		}
	}

	void testEqualityAssertWithNoArgsDelegatedToSeleniumInstance() {
		testCase.selenium.getLocation().returns("http://localhost:8080/foo")
		play {
			try {
				testCase.assertLocation("http://localhost:8080/foo")
			} catch (MissingMethodException e) {
				fail "Call to assertLocation was not delegated to Selenium.getLocation: $e.message"
			}
		}
	}

	void testBooleanVerifyDelegatesToSelenium() {
		testCase.selenium.isTextPresent("some string").returns(true)
		play {
			try {
				testCase.verifyTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testBooleanWaitForDelegatesToSelenium() {
		testCase.selenium.isTextPresent("some string").returns(true)
		play {
			try {
				testCase.waitForTextPresent("some string")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testEqualityVerifyDelegatesToSelenium() {
		testCase.selenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.verifyText("id=foo", "expected value")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testEqualityWaitForDelegatesToSelenium() {
		testCase.selenium.getText("id=foo").returns("expected value")
		play {
			try {
				testCase.waitForText("id=foo", "expected value")
			} catch (MissingMethodException e) {
				fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
			}
		}
	}

	void testAssertFailsCorrectly() {
		testCase.selenium.getText("id=foo").returns("not what I expected")
		play {
			shouldFail(AssertionFailedError) {
				testCase.assertText("id=foo", "expected value")
			}
		}
	}

	void testVerifyFailsCorrectly() {
		testCase.selenium.getText("id=foo").returns("not what I expected")
		play {
			testCase.verifyText("id=foo", "expected value")
			// TODO: SeleneseTestBase throws the wrong error type :(
			shouldFail(AssertionError) {
				testCase.checkForVerificationErrors()
			}
		}
	}

	void testWaitForFailsCorrectly() {
		testCase.selenium.getText("id=foo").returns("not what I expected")
		play {
			shouldFail(AssertionFailedError) {
				testCase.waitForText("id=foo", "expected value")
			}
		}
	}

}