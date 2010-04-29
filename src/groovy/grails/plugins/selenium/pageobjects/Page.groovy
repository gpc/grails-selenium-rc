package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.SeleniumAware
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.plugins.selenium.SeleniumHolder

@Mixin(SeleniumAware)
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
		SeleniumHolder.selenium.open(uri) // TODO: aargh! not the evil singleton
	}

	static String getContext() {
		ConfigurationHolder.config.grails.app.context
	}

	protected void validate() throws UnexpectedPageException {
		if (expectedTitle) {
			def title = selenium.title
			if (!(title ==~ expectedTitle)) {
				throw new UnexpectedPageException("Expected page title '$expectedTitle' but found '$title'")
			}
		}
	}
}