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
}

class TestFormPage extends GrailsFormPage {
	protected void validate() { }
}