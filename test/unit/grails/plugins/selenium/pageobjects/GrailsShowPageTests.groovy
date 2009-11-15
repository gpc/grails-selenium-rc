package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.SeleniumManager
import org.gmock.WithGMock

@WithGMock
class GrailsShowPageTests extends GroovyTestCase {

	def selenium
	GrailsShowPage page

	void setUp() {
		super.setUp()

		selenium = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = selenium

		page = new TestShowPage()
	}

	void testPropertyGetDelegatedToTable() {
		selenium.getXpathCount("//table/tbody/tr").returns(2)
		selenium.getTable("//table.0.0").returns("name")
		selenium.getTable("//table.0.1").returns("Rob")
		selenium.getTable("//table.1.0").returns("email")
		selenium.getTable("//table.1.1").returns("rob@energizedwork.com")
		play {
			assertEquals "Rob", page.name
			assertEquals "rob@energizedwork.com", page.email
		}
	}

	void testUnknownPropertiesHandledCorrectly() {
		selenium.getXpathCount("//table/tbody/tr").returns(1)
		selenium.getTable("//table.0.0").returns("name")
		play {
			shouldFail(MissingPropertyException) {
				page.foo
			}
		}
	}
}

class TestShowPage extends GrailsShowPage {}