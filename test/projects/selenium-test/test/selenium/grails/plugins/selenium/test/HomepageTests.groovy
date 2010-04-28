package grails.plugins.selenium.test

import grails.plugins.selenium.SeleniumAware

@Mixin(SeleniumAware)
class HomepageTests extends GroovyTestCase {

	void testHomepageLoads() {
		selenium.open "/"
		assertTrue selenium.isTextPresent("Welcome to Grails")
	}

}
