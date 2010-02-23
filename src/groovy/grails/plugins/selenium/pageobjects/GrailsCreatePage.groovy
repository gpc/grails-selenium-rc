package grails.plugins.selenium.pageobjects

/**
 * A page object for Grails scaffolded create pages.
 */
class GrailsCreatePage extends GrailsFormPage {

	static GrailsCreatePage open(String uri) {
		Page.open(uri)
		return new GrailsCreatePage()
	}

	GrailsShowPage save() {
		selenium.clickAndWait "css=.buttons input.save"
		return new GrailsShowPage()
	}

	GrailsCreatePage saveExpectingFailure() {
		selenium.clickAndWait "css=.buttons input.save"
		return this
	}

	protected void validate() {
		def title = selenium.title
		if (!(title ==~ /Create .+/)) {
			throw new InvalidPageStateException("Incorrect page with title '$title' found")
		}
	}

}