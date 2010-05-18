package grails.plugin.selenium.tests

import grails.plugins.selenium.SeleniumAware
import static org.hamcrest.MatcherAssert.*
import static org.hamcrest.Matchers.*
import static grails.plugins.selenium.condition.ClosureEvaluatingWait.*
import static java.util.concurrent.TimeUnit.*

@Mixin(SeleniumAware)
class ObservePageStateTests extends GroovyTestCase {

	void setUp() {
		super.setUp()
		selenium.open "/test"
	}

	void testGetWithNoArguments() {
		assertThat "result of getTitle()", selenium.title, equalTo("Selenium Obstacle Course")
	}

	void testGetWithLocator() {
		assertThat "result of getText(locator)", selenium.getText("css=.text-para p"), startsWith("Lorem ipsum dolor sit amet")
	}

	void testIsWithNoArguments() {
		assertFalse "result of isAlertPresent()", selenium.isAlertPresent()
	}

	void testIsWithLocator() {
		assertTrue "result of isTextPresent(text)", selenium.isTextPresent("Lorem ipsum dolor sit amet")
	}

	void testWaitForWithNoArguments() {
		selenium.click "css=.ajax-alert input[type=button]"
		selenium.waitForAlertPresent()
		assertThat selenium.alert, equalTo("O HAI!")
	}

	void testWaitForWithExpectation() {
		selenium.click "css=.ajax-message input[type=button]"
		selenium.waitForTextPresent("This is a message from an AJAX call!")
	}

	void testWaitForWithLocator() {
		selenium.click "css=.ajax-message input[type=button]"
		selenium.waitForElementPresent("css=#ajax-message-target .message")
	}

	void testWaitForWithLocatorAndExpectation() {
		selenium.click "css=.ajax-message input[type=button]"
		selenium.waitForText("css=#ajax-message-target .message", "This is a message from an AJAX call!")
	}

	void testWaitForWithLocatorAndMatcher() {
		selenium.click "css=.ajax-message input[type=button]"
		selenium.waitForText("css=#ajax-message-target .message", equalTo("This is a message from an AJAX call!"))
	}

	void testVisibilityDetection() {
		assertThat selenium.isVisible("css=img#chuck"), equalTo(false)
		selenium.click "css=input.show"
		selenium.waitForVisible("css=img#chuck")
		MILLISECONDS.sleep 500
		selenium.click "css=input.hide"
		waitFor("Chuck disappears") {
			!selenium.isVisible("css=img#chuck")
		}
	}

}
