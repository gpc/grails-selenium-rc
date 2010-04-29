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