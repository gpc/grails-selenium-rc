package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumManager
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock
import org.junit.After
import org.junit.Test
import grails.plugins.selenium.SeleniumTestContextHolder

@WithGMock
class PageTests extends GrailsUnitTestCase {

	@After
	void tearDown() {
		SeleniumTestContextHolder.context = null
	}

	@Test
	void openPrependsWebAppContextToUris() {
		mockConfig """
			grails.app.context = "/foo"
		"""

		def selenium = mock(Selenium) {
			open "/foo/pirate/list"
		}
		SeleniumTestContextHolder.context = new SeleniumManager(selenium: selenium)

		play {
			Page.open "/pirate/list"
		}
	}

}
