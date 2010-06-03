/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.SeleniumHolder
import grails.plugins.selenium.SeleniumWrapper
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.nullValue
import static org.junit.Assert.assertThat

@WithGMock
class GrailsFormPageTests {

	SeleniumWrapper mockSelenium
	GrailsFormPage page

	@Before
	void setUp() {
		mockSelenium = mock(SeleniumWrapper) {
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
			isElementPresent("aSelect").returns(true).stub()
			isElementPresent("aMultipleSelect").returns(true).stub()
			getAttribute("aSelect@multiple").returns(null).stub()
			getAttribute("aMultipleSelect@multiple").returns("multiple").stub()
		}

		SeleniumHolder.selenium = mockSelenium

		page = new TestFormPage()
	}

	@After
	void tearDown() {
		SeleniumHolder.selenium = null
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
		mockSelenium.isSomethingSelected("aSelect").returns(true)
		mockSelenium.getSelectedLabel("aSelect").returns("foo")
		play {
			assertThat page.aSelect, equalTo("foo")
		}
	}

	@Test
	void propertyGetRetrievesMultipleValuesFromSelectBox() {
		mockSelenium.isSomethingSelected("aMultipleSelect").returns(true)
		mockSelenium.getSelectedLabels("aMultipleSelect").returns(["foo", "bar", "baz"] as String[])
		play {
			assertThat page.aMultipleSelect, equalTo(["foo", "bar", "baz"])
		}
	}

	@Test
	void propertyGetRetrievesNullFromSelectBoxWithNoSelection() {
		mockSelenium.isSomethingSelected("aSelect").returns(false)
		play {
			assertThat page.aSelect, nullValue()
		}
	}

	@Test
	void propertyGetRetrievesEmptyListFromMultipleSelectBoxWithNoSelection() {
		mockSelenium.isSomethingSelected("aMultipleSelect").returns(false)
		play {
			assertThat page.aMultipleSelect, equalTo([])
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

class TestFormPage extends GrailsFormPage {

	TestFormPage() {
		super()
	}

	protected TestFormPage(String uri) {
		super(uri)
	}

	protected void verifyPage() { }
}