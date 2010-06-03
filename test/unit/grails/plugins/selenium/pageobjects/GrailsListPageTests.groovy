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
class GrailsListPageTests {

	SeleniumWrapper mockSelenium

	@Before
	void setUp() {
		mockSelenium = mock(SeleniumWrapper)
		SeleniumHolder.selenium = mockSelenium
	}

	@After
	void tearDown() {
		SeleniumHolder.selenium = null
	}

	@Test(expected = UnexpectedPageException)
	void constructorFailsIfWrongPageLoads() {
		mockSelenium.getTitle().returns("WTF Page is this?").stub()
		play {
			new GrailsListPage()
		}
	}

	@Test
	void columnNamesAreLazyLoaded() {
		mockSelenium.getTitle().returns("Thing List").stub()
		mockSelenium.getXpathCount("//table/thead/tr[1]/th").returns(3)
		(1..3).each {i ->
			mockSelenium.getText("//table/thead/tr/th[$i]").returns("Column $i" as String)
		}
		play {
			def page = new GrailsListPage()
			assertThat page.columnNames, equalTo(["Column 1", "Column 2", "Column 3"])
		}
	}

	@Test
	void getRowsScrapesTable() {
		mockSelenium.getTitle().returns("Thing List").stub()
		mockSelenium.getXpathCount("//table/thead/tr[1]/th").returns(3)
		mockSelenium.getXpathCount("//table/tbody/tr").returns(3)
		(1..3).each {i ->
			mockSelenium.getText("//table/thead/tr/th[$i]").returns("Column $i")
		}
		(1..3).each {y ->
			(1..3).each {x ->
				mockSelenium.getText("//table/tbody/tr[$y]/td[$x]").returns("$x.$y".toString())
			}
		}

		play {
			def page = new GrailsListPage()
			def rows = page.rows
			assertThat rows[0], equalTo(["Column 1": "1.1", "Column 2": "2.1", "Column 3": "3.1"])
			assertThat rows[1], equalTo(["Column 1": "1.2", "Column 2": "2.2", "Column 3": "3.2"])
			assertThat rows[2], equalTo(["Column 1": "1.3", "Column 2": "2.3", "Column 3": "3.3"])
		}
	}

}