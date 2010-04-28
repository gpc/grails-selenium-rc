package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.SeleniumHolder
import grails.plugins.selenium.SeleniumWrapper
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test

@WithGMock
class PageTests extends GrailsUnitTestCase {

	SeleniumWrapper mockSelenium

	@Before
	void setUp() {
		super.setUp()
		mockSelenium = mock(SeleniumWrapper)
		SeleniumHolder.selenium = mockSelenium
	}

	@After
	void tearDown() {
		super.tearDown()
		SeleniumHolder.selenium = null
	}

	@Test
	void openPrependsWebAppContextToUris() {
		mockConfig """
			grails.app.context = "/foo"
		"""

		mockSelenium.open("/foo/pirate/list")

		play {
			Page.open "/pirate/list"
		}
	}

}
