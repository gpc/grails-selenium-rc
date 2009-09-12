package selenium.test

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase

class HomepageTests extends GrailsSeleneseTestCase {

	void setUp() {
		super.setUp()
	}

	void testHomepageLoads() {
		selenium.open "/selenium-test/"
		assertTrue selenium.isTextPresent("Welcome to Grails")
	}

	void testHomepageLoadsAgain() {
		selenium.open "/selenium-test/"
		assertTrue selenium.isTextPresent("Welcome to Grails")
	}

}