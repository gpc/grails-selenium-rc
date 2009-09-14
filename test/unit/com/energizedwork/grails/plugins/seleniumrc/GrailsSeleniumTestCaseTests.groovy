package com.energizedwork.grails.plugins.seleniumrc

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase
import com.thoughtworks.selenium.Selenium
import grails.test.GrailsUnitTestCase

class GrailsSeleniumTestCaseTests extends GrailsUnitTestCase {

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

	void testBooleanAssertsDelegatedToSeleniumInstance() {
		def mockSelenium = mockFor(Selenium)
		mockSelenium.demand.isTextPresent() { String text ->
			println "IN UR MOCK"
			return true
		}
		GrailsSeleneseTestCase.selenium = mockSelenium.createMock()
		try {
			testCase.assertTextPresent("some string")
		} catch(MissingMethodException e) {
			fail "Call to assertTextPresent was not delegated to SeleneseTestBase: $e.message"
		}
		mockSelenium.verify()
	}

}