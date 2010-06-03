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
 * A page object for typical Grails scaffolded show pages.
 */
class GrailsShowPage extends GrailsPage {

	static GrailsShowPage open(String uri) {
		return new GrailsShowPage(uri)
	}

	GrailsShowPage() {
		super()
	}

	protected GrailsShowPage(String uri) {
		super(uri)
	}

	@Lazy List fieldNames = (0..<fieldCount).collect {i ->
		selenium.getTable("//table.$i.0").replaceAll(/[^\w\s]+/, "")
	}

	int getFieldCount() {
		selenium.getXpathCount("//table/tbody/tr")
	}

	/**
	 * Intercepts property getters to return data from table based on the field name.
	 */
	def propertyMissing(String name) {
		def i = fieldNames.indexOf(name)
		if (i >= 0) {
			selenium.getTable "//table.$i.1"
		} else {
			throw new MissingPropertyException(name)
		}
	}

	GrailsEditPage edit() {
		selenium.clickAndWait "css=.buttons input.edit"
	}

	GrailsListPage delete() {
		selenium.chooseOkOnNextConfirmation()
		selenium.click "css=.buttons input.delete"
		selenium.getConfirmation()
		selenium.waitForPageToLoad()
		return new GrailsListPage()
	}

	protected void verifyPage() {
		pageTitleMatches ~/Show \w+/
	}
}