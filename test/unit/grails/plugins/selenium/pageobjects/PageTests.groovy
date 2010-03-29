package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.Selenium
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
		
		SeleniumManager.instance.selenium = mock(Selenium) {
			open "/foo/pirate/list"
		}
		
		play {
			Page.open "/pirate/list"
		}
	}
	
}
