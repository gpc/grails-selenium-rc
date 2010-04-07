package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumManager
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import grails.plugins.selenium.SeleniumTestContextHolder
import grails.plugins.selenium.SeleniumTestContext

@WithGMock
class GrailsShowPageTests {

	Selenium mockSelenium

	@Before
	void setUp() {
		mockSelenium = mock(Selenium)
		mockSelenium.getTitle().returns("Show Thing").stub()
		SeleniumTestContextHolder.context = mock(SeleniumTestContext)
		SeleniumTestContextHolder.context.getSelenium().returns(mockSelenium).stub()
	}

	@After
	void tearDown() {
		SeleniumTestContextHolder.context = null
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