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

		assertEquals "localhost", seleniumManager.config.selenium.server.host
		assertEquals 60000, seleniumManager.config.selenium.defaultTimeout
		assertFalse seleniumManager.config.selenium.slow
	}

	void testDefaultConfigOverriddenWhenSeleniumConfigFileFound() {
	}

	void testDefaultConfigOverriddenBySystemProperties() {
		def props = [
				"selenium.browser": "*googlechrome",
				"selenium.defaultTimeout": "5000",
				"selenium.slow": "true"
		] as Properties
		mock(System).static.properties.returns(props).atLeastOnce()

		play {
			seleniumManager.loadConfig()
		}

		assertEquals "*googlechrome", seleniumManager.config.selenium.browser
		assertEquals 5000, seleniumManager.config.selenium.defaultTimeout
		assertTrue seleniumManager.config.selenium.slow
	}
	
	void testNestedConfigOverriddenBySystemProperties() {
		def props = [
			"selenium.server.host": "my.host.bv",
			"selenium.server.port": "1234"
		] as Properties
		mock(System).static.properties.returns(props).atLeastOnce()
		
		play {
			seleniumManager.loadConfig()
		}
		
		assertEquals "my.host.bv", seleniumManager.config.selenium.server.host
		assertEquals 1234, seleniumManager.config.selenium.server.port
	}

}