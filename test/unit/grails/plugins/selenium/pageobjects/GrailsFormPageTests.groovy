package grails.plugins.selenium.pageobjects

import org.gmock.WithGMock
import grails.plugins.selenium.SeleniumManager
import grails.plugins.selenium.GrailsSelenium

@WithGMock
class GrailsFormPageTests extends GroovyTestCase {

	def mockSelenium
	GrailsFormPage page

	void setUp() {
		super.setUp()

		mockSelenium = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = mockSelenium

		page = new TestFormPage()
	}

	void testPropertySetIsDelegatedToFormField() {
		mockSelenium.type("foo", "bar")
		play {
			page.foo = "bar"
		}
	}

	void testPropertyGetIsDelegatedToFormField() {
		mockSelenium.getValue("foo").returns("bar")
		play {
			assertEquals "bar", page.foo
		}
	}

	void testErrorMessagesAreOnlyRetrievedOnce() {
		(1..3).each { i->
			mockSelenium.isElementPresent("css=.errors ul li:nth-child($i)").returns(true)
			mockSelenium.getText("css=.errors ul li:nth-child($i)").returns("error $i")
		}
		mockSelenium.isElementPresent("css=.errors ul li:nth-child(4)").returns(false)
		play {
			2.times {
				assertEquals(["error 1", "error 2", "error 3"], page.errorMessages)
			}
		}
	}
}

class TestFormPage extends GrailsFormPage {}