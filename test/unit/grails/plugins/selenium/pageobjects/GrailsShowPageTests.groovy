package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.SeleniumHolder
import grails.plugins.selenium.SeleniumWrapper
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

@WithGMock
class GrailsShowPageTests {

	SeleniumWrapper mockSelenium

	@Before
	void setUp() {
		mockSelenium = mock(SeleniumWrapper)
		mockSelenium.getTitle().returns("Show Thing").stub()
		SeleniumHolder.selenium = mockSelenium
	}

	@After
	void tearDown() {
		SeleniumHolder.selenium = null
	}

	@Test
	void propertyGetDelegatedToTable() {
		mockSelenium.getXpathCount("//table/tbody/tr").returns(2)
		mockSelenium.getTable("//table.0.0").returns("name")
		mockSelenium.getTable("//table.0.1").returns("Rob")
		mockSelenium.getTable("//table.1.0").returns("email")
		mockSelenium.getTable("//table.1.1").returns("rob@energizedwork.com")
		play {
			def page = new GrailsShowPage()
			assertThat page.name, equalTo("Rob")
			assertThat page.email, equalTo("rob@energizedwork.com")
		}
	}

	@Test(expected = MissingPropertyException)
	void unknownPropertiesHandledCorrectly() {
		mockSelenium.getXpathCount("//table/tbody/tr").returns(1)
		mockSelenium.getTable("//table.0.0").returns("name")
		play {
			def page = new GrailsShowPage()
			page.foo
		}
	}

	@Test(expected = MissingPropertyException)
	void propertySetDoesNotWork() {
		mockSelenium.getXpathCount("//table/tbody/tr").returns(1)
		mockSelenium.getTable("//table.0.0").returns("name")
		play {
			def page = new GrailsShowPage()
			page.name = "Rob"
		}
	}
}