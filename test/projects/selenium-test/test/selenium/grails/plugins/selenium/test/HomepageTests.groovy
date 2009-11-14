package grails.plugins.selenium.test

import grails.plugins.selenium.*

@Mixin(SeleniumTest)
class HomepageTests extends GroovyTestCase {

	void testHomepageLoads() {
		selenium.open "/"
		assertTrue selenium.isTextPresent("Welcome to Grails")
	}

}