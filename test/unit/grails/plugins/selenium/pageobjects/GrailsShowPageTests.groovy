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