package grails.plugins.selenium.pageobjects

class UnexpectedPageException extends IllegalStateException {

	UnexpectedPageException(String message) {
		super(message)
	}
}