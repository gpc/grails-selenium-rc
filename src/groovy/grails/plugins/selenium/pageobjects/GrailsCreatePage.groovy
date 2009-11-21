package grails.plugins.selenium.pageobjects

/**
 * A page object for Grails scaffolded create pages.
 */
class GrailsCreatePage extends GrailsFormPage {

	static GrailsCreatePage open(String url) {
		def page = new GrailsCreatePage()
		page.selenium.open url
		return page
	}

	GrailsShowPage save() {
		selenium.clickAndWait "css=.buttons input.save"
		return new GrailsShowPage()
	}

	GrailsCreatePage saveExpectingFailure() {
		selenium.clickAndWait "css=.buttons input.save"
		return this
	}

}