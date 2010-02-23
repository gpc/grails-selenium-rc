package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.SeleniumManager
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock

@WithGMock
class PageTests extends GrailsUnitTestCase {
	
	void testOpenPrependsWebAppContextToUris() {
		mockConfig """
			grails.app.context = "/foo"
		"""
		
		SeleniumManager.instance.selenium = mock(GrailsSelenium) {
			open("/foo/pirate/list")
		}
		
		play {
			Page.open("/pirate/list")
		}
	}
	
}
