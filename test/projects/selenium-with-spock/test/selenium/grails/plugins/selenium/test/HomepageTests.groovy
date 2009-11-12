package grails.plugins.selenium.test

import grails.plugins.selenium.*

class HomepageTests extends GrailsSeleniumTestCase {

	def testUserCanLoadApplicationHomePage() {
		selenium.open "/"
		assertTrue selenium.isTextPresent("Welcome to Grails")
	}
	
}
