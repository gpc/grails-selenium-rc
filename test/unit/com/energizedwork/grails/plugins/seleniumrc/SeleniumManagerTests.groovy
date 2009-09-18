package com.energizedwork.grails.plugins.seleniumrc

import grails.test.GrailsUnitTestCase

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