package grails.plugins.selenium.pageobjects

class InvalidPageStateException extends IllegalStateException {

	InvalidPageStateException(String message) {
		super(message)
	}
}