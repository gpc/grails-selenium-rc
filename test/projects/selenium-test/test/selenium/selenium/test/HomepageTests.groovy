package selenium.test

import com.thoughtworks.selenium.SeleneseTestCase
import com.thoughtworks.selenium.GroovySeleneseTestCase

class HomepageTests extends GroovySeleneseTestCase {

	void setUp() {
		super.setUp("http://localhost:8080/selenium-test/", "*firefox")
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