package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.SeleniumManager

/**
 * A base page object for scaffolded Grails pages.
 */
abstract class GrailsPage {

	protected final GrailsSelenium selenium

	GrailsPage() {
		this.selenium = SeleniumManager.instance.selenium
		validate()
	}

	static void open(String uri) {
		SeleniumManager.instance.selenium.open(uri)
	}

	void open() {
		selenium.open(uri)
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

	protected abstract void validate();
}