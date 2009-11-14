package grails.plugins.selenium.test.pageobjects

import grails.plugins.selenium.GrailsSelenium
import grails.plugins.selenium.SeleniumManager

abstract class Page {

	protected final GrailsSelenium selenium

	Page() {
		this.selenium = SeleniumManager.instance.selenium
	}

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
	 * Returns standard Grails flash message text if present, otherwise null.
	 */
	String getFlashMessage() {
		selenium.isElementPresent("css=.message") ? selenium.getText("css=.message") : null
	}

	/**
	 * Returns true if named form field is highlighted for errors.
	 */
	boolean hasFieldErrors(String field) {
		selenium.isElementPresent "css=.errors input[name=$field]"
	}

	/**
	 * Intercepts property getters to return value of form field
	 */
	def propertyMissing(String name) {
		selenium.getValue(name)
	}

	/**
	 * Intercepts property setters to type in form field
	 */
	def propertyMissing(String name, value) {
		selenium.type(name, value)
	}

}