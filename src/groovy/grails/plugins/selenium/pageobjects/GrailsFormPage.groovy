package grails.plugins.selenium.pageobjects

/**
 * A base page object for typical Grails form pages (e.g. scaffolded create and edit pages).
 */
abstract class GrailsFormPage extends GrailsPage {

	/**
	 * Returns standard Grails form error messages if present, otherwise empty list.
	 */
	List getErrorMessages() {
		def messages = []
		def i = 1
		while (selenium.isElementPresent("css=.errors ul li:nth-child($i)")) {
			messages << selenium.getText("css=.errors ul li:nth-child($i)")
			i++
		}
		return messages
	}

	/**
	 * Returns true if the named form input is highlighted for errors.
	 */
	boolean hasFieldErrors(String field) {
		selenium.isElementPresent "css=.errors input[name=$field]"
	}

	/**
	 * Intercepts property getters to return value of form field.
	 */
	def propertyMissing(String name) {
		selenium.getValue(name)
	}

	/**
	 * Intercepts property setters to type in form field.
	 */
	def propertyMissing(String name, value) {
		selenium.type(name, value)
	}

}