package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleneseTestCase

class HomepageTests extends GrailsSeleneseTestCase {

	void testHomepageLoads() {
		selenium.open "$rootURL/"
		assertTextPresent "Welcome to Grails"
	}

}