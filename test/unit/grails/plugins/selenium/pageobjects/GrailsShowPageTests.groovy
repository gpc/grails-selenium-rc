package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.SeleniumManager
import org.gmock.WithGMock

@WithGMock
class GrailsShowPageTests extends GroovyTestCase {

	def selenium

	void setUp() {
		super.setUp()

		selenium = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = selenium
	}

	void testPropertyGetDelegatedToTable() {
		selenium.getTitle().returns("Show Thing")
		selenium.getXpathCount("//table/tbody/tr").returns(2)
		selenium.getTable("//table.0.0").returns("name")
		selenium.getTable("//table.0.1").returns("Rob")
		selenium.getTable("//table.1.0").returns("email")
		selenium.getTable("//table.1.1").returns("rob@energizedwork.com")
		play {
			def page = new GrailsShowPage()
			assertEquals "Rob", page.name
			assertEquals "rob@energizedwork.com", page.email
		}
	}

	void testUnknownPropertiesHandledCorrectly() {
		selenium.getTitle().returns("Show Thing")
		selenium.getXpathCount("//table/tbody/tr").returns(1)
		selenium.getTable("//table.0.0").returns("name")
		play {
			def page = new GrailsShowPage()
			shouldFail(MissingPropertyException) {
				page.foo
			}
		}
	}
}