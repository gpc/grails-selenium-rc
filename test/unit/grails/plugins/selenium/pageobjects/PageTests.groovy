package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.SeleniumManager
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock
import org.junit.Test

@WithGMock
class PageTests extends GrailsUnitTestCase {
	
	@Test void openPrependsWebAppContextToUris() {
		mockConfig """
			grails.app.context = "/foo"
		"""
		
		SeleniumManager.instance.selenium = mock(GrailsSelenium) {
			open "/foo/pirate/list"
		}
		
		play {
			Page.open "/pirate/list"
		}
	}
	
}
