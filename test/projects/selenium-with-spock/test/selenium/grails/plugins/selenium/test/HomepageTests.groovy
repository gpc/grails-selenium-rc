package grails.plugins.selenium.test

import grails.plugins.selenium.*

class HomepageTests extends GrailsSeleneseTestCase {

	def testUserCanLoadApplicationHomePage() {
		selenium.open(rootURL)
		assertTrue selenium.isTextPresent("Welcome to Grails")
	}
	
}
