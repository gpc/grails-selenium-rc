package selenium.test

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase

class HomepageTests extends GrailsSeleneseTestCase {

	void testHomepageLoads() {
		selenium.open "$rootURL/"
		assertTextPresent "Welcome to Grails"
	}

}