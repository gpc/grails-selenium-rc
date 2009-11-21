package grails.plugins.selenium.pageobjects

/**
 * A base page object for scaffolded Grails form pages (i.e. create & edit).
 */
abstract class GrailsFormPage extends GrailsPage {

	/**
	 * Returns standard Grails form error messages if present, otherwise empty list.
	 */
	List getErrorMessages() {
		def errorCount = selenium.getXpathCount("//div[@class='errors']/ul/li")
		if (errorCount > 0) {
			return (1..errorCount).collect {i ->
				selenium.getText "//div[@class='errors']/ul/li[$i]"
			}
		} else {
			return []
		}
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