package grails.plugins.selenium.pageobjects

/**
 * A page object for Grails scaffolded create pages.
 */
class GrailsCreatePage extends GrailsFormPage {

	static GrailsCreatePage open(String uri) {
		return new GrailsCreatePage(uri)
	}

	GrailsCreatePage() {
		super()
	}

	protected GrailsCreatePage(String uri) {
		super(uri)
	}

	GrailsShowPage save() {
		selenium.clickAndWait "css=.buttons input.save"
		return new GrailsShowPage()
	}

	GrailsCreatePage saveExpectingFailure() {
		selenium.clickAndWait "css=.buttons input.save"
		return this
	}

	protected void verifyPage() {
		pageTitleMatches ~/Create \w+/
	}
}