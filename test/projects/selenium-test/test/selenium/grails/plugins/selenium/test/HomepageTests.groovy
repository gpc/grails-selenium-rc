package grails.plugins.selenium.test

import grails.plugins.selenium.Selenese

@Mixin(Selenese)
class HomepageTests extends GroovyTestCase {

	void testHomepageLoads() {
		selenium.open "$contextPath/"
		assertTrue selenium.isTextPresent("Welcome to Grails")
	}

}