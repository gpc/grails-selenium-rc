package grails.plugins.selenium.pageobjects

import grails.plugins.selenium.SeleniumManager
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.plugins.selenium.GrailsSelenium

abstract class Page {
	
	protected final GrailsSelenium selenium

	Page() {
		this.selenium = SeleniumManager.instance.selenium
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

	protected abstract void validate() throws InvalidPageStateException;
}