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
 * A base page object for scaffolded Grails pages.
 */
abstract class GrailsPage extends Page {

	GrailsPage() {
		super()
	}

	protected GrailsPage(String uri) {
		super(uri)
	}

	/**
	 * Returns standard Grails flash message text if present, otherwise null.
	 */
	String getFlashMessage() {
		hasFlashMessage() ? selenium.getText("css=.message") : null
	}

	/**
	 * Returns true if there is a flash message present on the page.
	 */
	boolean hasFlashMessage() {
		return selenium.isElementPresent("css=.message")
	}

	GrailsCreatePage goToCreate() {
		selenium.clickAndWait ".nav a.create"
		return new GrailsCreatePage()
	}

	GrailsListPage goToList() {
		selenium.clickAndWait ".nav a.list"
		return new GrailsListPage()
	}
}