package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumTestContextHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

abstract class Page {

	private final String expectedTitle

	Page() {
		this(null)
	}

	Page(String expectedTitle) {
		this.expectedTitle = expectedTitle
		validate()
	}

	static void open(String uri) {
		if (!uri.startsWith(context)) {
			uri = context + uri
		}
		SeleniumTestContextHolder.context.selenium.open(uri)
	}

	static String getContext() {
		ConfigurationHolder.config.grails.app.context
	}

	protected Selenium getSelenium() {
		return SeleniumTestContextHolder.context.selenium
	}

	protected void validate() throws InvalidPageStateException {
		if (expectedTitle) {
			def title = selenium.title
			if (!(title ==~ expectedTitle)) {
				throw new InvalidPageStateException("Expected page title '$expectedTitle' but found '$title'")
			}
		}
	}
}