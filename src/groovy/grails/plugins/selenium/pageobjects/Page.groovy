package grails.plugins.selenium.pageobjects

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumManager
import org.codehaus.groovy.grails.commons.ConfigurationHolder

abstract class Page {

	protected final Selenium selenium
	private final String expectedTitle

	Page() {
		this(null)
	}

	Page(String expectedTitle) {
		this.selenium = SeleniumManager.instance.selenium
		this.expectedTitle = expectedTitle
		validate()
	}

	static void open(String uri) {
		if (!uri.startsWith(context)) {
			uri = context + uri
		}
		SeleniumManager.instance.selenium.open(uri)
	}

	static String getContext() {
		ConfigurationHolder.config.grails.app.context
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