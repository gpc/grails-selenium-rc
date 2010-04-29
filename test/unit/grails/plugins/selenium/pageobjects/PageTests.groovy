package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.SeleniumHolder
import grails.plugins.selenium.SeleniumWrapper
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test

@WithGMock
class PageTests {

	SeleniumWrapper mockSelenium

	@Before
	void setUp() {
		mockSelenium = mock(SeleniumWrapper)
		SeleniumHolder.selenium = mockSelenium
	}

	@After
	void tearDown() {
		SeleniumHolder.selenium = null
	}

	@Test
	void uriConstructorPrependsWebAppContextToUris() {
		mockSelenium.getContextPath().returns("/foo").stub()
		mockSelenium.open("/foo/pirate/list")

		play {
			new DummyPage("/pirate/list")
		}
	}

}

class DummyPage extends Page {

	DummyPage() {
		super()
	}

	protected DummyPage(String uri) {
		super(uri)
	}

	protected void verifyPage() {}
}
