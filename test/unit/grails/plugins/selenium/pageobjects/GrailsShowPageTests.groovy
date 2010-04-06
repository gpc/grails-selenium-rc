package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumManager
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

@WithGMock
class GrailsShowPageTests {

	def selenium

	@Before
	void setUp() {
		selenium = mock(Selenium)
		selenium.getTitle().returns("Show Thing").stub()
		SeleniumManager.instance = new SeleniumManager(selenium: selenium)
	}

	@After
	void tearDown() {
		SeleniumManager.instance = null
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