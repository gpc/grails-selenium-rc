package com.energizedwork.grails.plugins.seleniumrc

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase
import com.thoughtworks.selenium.Selenium
import grails.test.GrailsUnitTestCase

class GrailsSeleneseTestCaseTests extends GrailsUnitTestCase {

	GrailsSeleneseTestCase testCase

	void setUp() {
		super.setUp()

		testCase = new GrailsSeleneseTestCase()
	}

	void testMethodsDelegateToSeleneseTestBase() {
		try {
			testCase.verifyTrue(true)
		} catch(MissingMethodException e) {
			fail "Call to verifyTrue was not delegated to SeleneseTestBase"
		}
	}

	void testBooleanAssertWithOneArgDelegatedToSeleniumInstance() {
		def mockSelenium = mockFor(Selenium)
		mockSelenium.demand.isTextPresent() { String text -> true }
		GrailsSeleneseTestCase.selenium = mockSelenium.createMock()
		try {
			testCase.assertTextPresent("some string")
		} catch(MissingMethodException e) {
			fail "Call to assertTextPresent was not delegated to Selenium.isTextPresent: $e.message"
		}
		mockSelenium.verify()
	}

	void testBooleanAssertWithNoArgsDelegatedToSeleniumInstance() {
		def mockSelenium = mockFor(Selenium)
		mockSelenium.demand.isAlertPresent() { -> true }
		GrailsSeleneseTestCase.selenium = mockSelenium.createMock()
		try {
			testCase.assertAlertPresent()
		} catch(MissingMethodException e) {
			fail "Call to assertlertPresent was not delegated to Selenium.isAlertPresent: $e.message"
		}
		mockSelenium.verify()
	}

	void testEqualityAssertWithOneArgDelegatedToSeleniumInstance() {
		def mockSelenium = mockFor(Selenium)
		mockSelenium.demand.getText() { String locator -> "expected value" }
		GrailsSeleneseTestCase.selenium = mockSelenium.createMock()
		try {
			testCase.assertText("id=foo", "expected value")
		} catch(MissingMethodException e) {
			fail "Call to assertText was not delegated to Selenium.getText: $e.message"
		}
		mockSelenium.verify()
	}

	void testEqualityAssertWithNoArgsDelegatedToSeleniumInstance() {
		def mockSelenium = mockFor(Selenium)
		mockSelenium.demand.getLocation() { -> "http://localhost:8080/foo" }
		GrailsSeleneseTestCase.selenium = mockSelenium.createMock()
		try {
			testCase.assertLocation("http://localhost:8080/foo")
		} catch(MissingMethodException e) {
			fail "Call to assertLocation was not delegated to Selenium.getLocation: $e.message"
		}
		mockSelenium.verify()
	}

	void testVerifyDelegatesSameAsAssert() {
		def mockSelenium = mockFor(Selenium)
		mockSelenium.demand.isTextPresent() { String text -> true }
		GrailsSeleneseTestCase.selenium = mockSelenium.createMock()
		try {
			testCase.verifyTextPresent("some string")
		} catch(MissingMethodException e) {
			fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
		}
		mockSelenium.verify()
	}

	void testWaitForDelegatesSameAsAssert() {
		def mockSelenium = mockFor(Selenium)
		mockSelenium.demand.isTextPresent() { String text -> true }
		GrailsSeleneseTestCase.selenium = mockSelenium.createMock()
		try {
			testCase.waitForTextPresent("some string")
		} catch(MissingMethodException e) {
			fail "Call to verifyTextPresent was not delegated to Selenium.isTextPresent: $e.message"
		}
		mockSelenium.verify()
	}

}