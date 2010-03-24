package grails.plugins.selenium.pageobjects

import org.gmock.WithGMock
import grails.plugins.selenium.SeleniumManager
import grails.plugins.selenium.GrailsSelenium
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*

@WithGMock
class GrailsFormPageTests {

	GrailsSelenium mockSelenium
	GrailsFormPage page

	@Before
	void setUp() {
		mockSelenium = mock(GrailsSelenium) {
			isElementPresent("css=input[name=aTextField]").returns(true).stub()
			isElementPresent("css=input[name=aHiddenField]").returns(true).stub()
			isElementPresent("css=input[name=aCheckbox]").returns(true).stub()
			isElementPresent("css=input[name=anUnspecifiedField]").returns(true).stub()
			isElementPresent("css=input[name=aSelect]").returns(false).stub()
			isElementPresent("css=input[name=aMultipleSelect]").returns(false).stub()
			isElementPresent("css=select[name=aSelect]").returns(true).stub()
			isElementPresent("css=select[name=aMultipleSelect]").returns(true).stub()
			getAttribute("css=input[name=aTextField]@type").returns("text").stub()
			getAttribute("css=input[name=aHiddenField]@type").returns("hidden").stub()
			getAttribute("css=input[name=aCheckbox]@type").returns("checkbox").stub()
			getAttribute("css=input[name=anUnspecifiedField]@type").returns(null).stub()
			getAttribute("css=select[name=aSelect]@multiple").returns(null).stub()
			getAttribute("css=select[name=aMultipleSelect]@multiple").returns("multiple").stub()
		}

		SeleniumManager.instance.selenium = mockSelenium

		page = new TestFormPage()
	}

	@Test
	void propertySetIsDelegatedToFormField() {
		mockSelenium.type("aTextField", "foo")
		play {
			page.aTextField = "foo"
		}
	}

	@Test
	void propertyGetIsDelegatedToFormField() {
		mockSelenium.getValue("aTextField").returns("foo")
		play {
			assertThat page.aTextField, equalTo("foo")
		}
	}

	@Test
	void propertySetSelectsValueInSelectBox() {
		mockSelenium.select("aSelect", "foo")
		play {
			page.aSelect = "foo"
		}
	}

	@Test
	void propertySetSelectsMultipleValueInSelectBox() {
		mockSelenium.removeAllSelections("aMultipleSelect")
		mockSelenium.addSelection("aMultipleSelect", "foo")
		mockSelenium.addSelection("aMultipleSelect", "bar")
		mockSelenium.addSelection("aMultipleSelect", "baz")
		play {
			page.aMultipleSelect = ["foo", "bar", "baz"]
		}
	}

	@Test
	void propertyGetRetrievesValueFromSelectBox() {
		mockSelenium.getSelectedValue("aSelect").returns("foo")
		play {
			assertThat page.aSelect, equalTo("foo")
		}
	}

	@Test
	void propertyGetRetrievesMultipleValuesFromSelectBox() {
		mockSelenium.getSelectedValues("aMultipleSelect").returns(["foo", "bar", "baz"] as String[])
		play {
			assertThat page.aMultipleSelect, equalTo(["foo", "bar", "baz"] as String[])
		}
	}

	@Test
	void propertySetChecksCheckbox() {
		mockSelenium.check("aCheckbox")
		play {
			page.aCheckbox = true
		}
	}

	@Test
	void propertySetUnchecksCheckbox() {
		mockSelenium.uncheck("aCheckbox")
		play {
			page.aCheckbox = false
		}
	}

	@Test
	void propertyGetRetrievesCheckedStateofCheckbox() {
		mockSelenium.isChecked("aCheckbox").returns(true)
		play {
			assertThat page.aCheckbox, equalTo(true)
		}
	}

	@Test
	void canGetValueOfHiddenField() {
		mockSelenium.getValue("aHiddenField").returns("foo")
		play {
			assertThat page.aHiddenField, equalTo("foo")
		}
	}

	@Test(expected = ReadOnlyPropertyException)
	void cannotSetValueOfHiddenField() {
		play {
			page.aHiddenField = "foo"
		}
	}

	@Test
	void inputWithUnspecifiedTypeIsTreatedAsText() {
		mockSelenium.type("anUnspecifiedField", "foo")
		mockSelenium.getValue("anUnspecifiedField").returns("foo")
		play {
			page.anUnspecifiedField = "foo"
			assertThat page.anUnspecifiedField, equalTo("foo")
		}
	}

}

class TestFormPage extends GrailsFormPage {}