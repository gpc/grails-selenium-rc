package grails.plugins.selenium

import grails.test.GrailsUnitTestCase
import grails.plugins.selenium.SeleniumManager

class SeleniumManagerTests extends GrailsUnitTestCase {

	SeleniumManager seleniumManager

	void setUp() {
		super.setUp()
		seleniumManager = SeleniumManager.instance
	}

	void testDefaultConfigGetsLoaded() {
		seleniumManager.loadConfig()

		assertEquals "localhost", seleniumManager.config.selenium.host
		assertEquals 4444, seleniumManager.config.selenium.port
	}

	void testDefaultConfigOverriddenWhenSeleniumConfigFileFound() {
		
	}

}