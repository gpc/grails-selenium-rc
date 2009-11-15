package grails.plugins.selenium

import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock

@WithGMock
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

	void testDefaultConfigOverriddenBySystemProperties() {
		def props = [
				"selenium.host": "my.host.bv",
				"selenium.port": "1234",
				"selenium.slow": "true"
		]
		mock(System).static.properties.returns(props).atLeastOnce()

		play {
			seleniumManager.loadConfig()
		}

		assertEquals "my.host.bv", seleniumManager.config.selenium.host
		assertEquals 1234, seleniumManager.config.selenium.port
		assertTrue seleniumManager.config.selenium.slow
	}

}