package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.SeleniumManager
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*

@WithGMock
class GrailsShowPageTests {

	def selenium

	@Before
	void setUp() {
		selenium = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = selenium
		selenium.getTitle().returns("Show Thing").stub()
	}

	@Test
	void propertyGetDelegatedToTable() {
		selenium.getXpathCount("//table/tbody/tr").returns(2)
		selenium.getTable("//table.0.0").returns("name")
		selenium.getTable("//table.0.1").returns("Rob")
		selenium.getTable("//table.1.0").returns("email")
		selenium.getTable("//table.1.1").returns("rob@energizedwork.com")
		play {
			def page = new GrailsShowPage()
			assertThat page.name, equalTo("Rob")
			assertThat page.email, equalTo("rob@energizedwork.com")
		}
	}

	@Test(expected = MissingPropertyException)
	void unknownPropertiesHandledCorrectly() {
		selenium.getXpathCount("//table/tbody/tr").returns(1)
		selenium.getTable("//table.0.0").returns("name")
		play {
			def page = new GrailsShowPage()
			page.foo
		}
	}

	@Test(expected = MissingPropertyException)
	void propertySetDoesNotWork() {
		selenium.getXpathCount("//table/tbody/tr").returns(1)
		selenium.getTable("//table.0.0").returns("name")
		play {
			def page = new GrailsShowPage()
			page.name = "Rob"
		}
	}
}