package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleniumTestCase

/**
 * Previously had an issue where using another class between GrailsSeleniumTestCase and the actual test would cause a
 * StackOverflowError when setUp was called. This prevents a regression.
 */
abstract class AbstractTabsTestCase extends GrailsSeleniumTestCase {

	protected void assertTabSelected(int i) {
		assertNotNull selenium.getAttribute("//div[@id='tabs']/ul/li[$i]@class") =~ /\bui-tabs-selected\b/
	}

}