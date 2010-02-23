package grails.plugins.selenium.pageobjects

/**
 * A page object for Grails scaffolded edit pages.
 */
class GrailsEditPage extends GrailsFormPage {

	static GrailsEditPage open(String uri) {
		GrailsPage.open(uri)
		return new GrailsEditPage()
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
		selenium.waitForPageToLoad "$selenium.defaultTimeout" 
		return new GrailsListPage()
	}

	protected void validate() {
		def title = selenium.title
		if (!(title ==~ /Create .+/)) {
			throw new InvalidPageStateException("Incorrect page with title '$title' found")
		}
	}

}