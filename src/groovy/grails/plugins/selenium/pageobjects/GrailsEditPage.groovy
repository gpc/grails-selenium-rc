package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.Wait

/**
 * A page object for Grails scaffolded edit pages.
 */
class GrailsEditPage extends GrailsFormPage {

	static GrailsEditPage open(String uri) {
		Page.open(uri)
		return new GrailsEditPage()
	}

	GrailsEditPage() {
		super(/Edit \w+/)
	}

	GrailsEditPage(String expectedTitle) {
		super(expectedTitle)
	}

	GrailsShowPage save() {
		selenium.clickAndWait "css=.buttons input.save"
		return new GrailsShowPage()
	}

	GrailsEditPage saveExpectingFailure() {
		selenium.clickAndWait "css=.buttons input.save"
		return this
	}

	GrailsListPage delete() {
		selenium.chooseOkOnNextConfirmation()
		selenium.click "css=.buttons input.delete"
		selenium.getConfirmation()
		selenium.waitForPageToLoad "$Wait.DEFAULT_TIMEOUT" // TODO: need to use config here 
		return new GrailsListPage()
	}

}