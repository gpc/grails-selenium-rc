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

/**
 * A page object for Grails scaffolded list pages.
 */
class GrailsListPage extends GrailsPage {

	static GrailsListPage open(String uri) {
		return new GrailsListPage(uri)
	}

	GrailsListPage() {
		super()
	}

	protected GrailsListPage(String uri) {
		super(uri)
	}

	@Lazy List columnNames = (1..columnCount).collect {i ->
		selenium.getText("//table/thead/tr/th[$i]")
	}

	/**
	 * Returns the number of columns in the table.
	 */
	int getColumnCount() {
		selenium.getXpathCount("//table/thead/tr[1]/th")
	}

	/**
	 * Returns the number of rows in the table body (not including any <tt>thead</tt> rows).
	 */
	int getRowCount() {
		selenium.getXpathCount("//table/tbody/tr")
	}

	/**
	 * Gets the data from the list table, each element in the list is a Map whose keys correspond to the column names
	 * and whose values are the text contained in the table cells on the page.
	 */
	List getRows() {
		(1..rowCount).collect {row ->
			def data = [:]
			columnNames.eachWithIndex {key, column ->
				data."$key" = selenium.getText("//table/tbody/tr[$row]/td[${column + 1}]")
			}
			return data
		}
	}

	/**
	 * Clicks the column heading to sort the table.
	 */
	GrailsListPage sortByColumn(String column) {
		selenium.clickAndWait "link=$column"
		return this
	}

	GrailsListPage nextPage() {
		selenium.clickAndWait "css=a.nextLink"
		return this
	}

	GrailsListPage previousPage() {
		selenium.clickAndWait "css=a.prevLink"
		return this
	}

	protected void verifyPage() {
		pageTitleMatches ~/\w+ List/
	}
}