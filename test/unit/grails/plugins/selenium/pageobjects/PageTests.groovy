package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumTestContext
import grails.plugins.selenium.SeleniumTestContextHolder
import grails.test.GrailsUnitTestCase
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test

@WithGMock
class PageTests extends GrailsUnitTestCase {

	Selenium mockSelenium

	@Before
	void setUp() {
		super.setUp()
		mockSelenium = mock(Selenium)
		SeleniumTestContextHolder.context = mock(SeleniumTestContext)
		SeleniumTestContextHolder.context.getSelenium().returns(mockSelenium).stub()
	}

	@After
	void tearDown() {
		super.tearDown()
		SeleniumTestContextHolder.context = null
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
