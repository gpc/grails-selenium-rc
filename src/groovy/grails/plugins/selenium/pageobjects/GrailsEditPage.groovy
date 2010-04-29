package grails.plugins.selenium.pageobjects

/**
 * A page object for Grails scaffolded edit pages.
 */
class GrailsEditPage extends GrailsFormPage {

	static GrailsEditPage open(String uri) {
		return new GrailsEditPage(uri)
	}

	GrailsEditPage() {
		super()
	}

	protected GrailsEditPage(String uri) {
		super(uri)
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
		selenium.waitForPageToLoad() 
		return new GrailsListPage()
	}

	protected void verifyPage() {
		pageTitleMatches ~/Edit \w+/
	}
}