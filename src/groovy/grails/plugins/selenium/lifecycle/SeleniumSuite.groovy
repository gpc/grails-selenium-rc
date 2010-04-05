package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.events.EventHandlerSupport
import grails.plugins.selenium.SeleniumTestContext
import grails.plugins.selenium.events.EventHandler

class SeleniumSuite extends EventHandlerSupport {

	SeleniumServerRunner seleniumServerRunner
	SeleniumRunner seleniumRunner

	SeleniumSuite(SeleniumTestContext context, SeleniumServerRunner seleniumServerRunner, SeleniumRunner seleniumRunner) {
		super(context, [EventHandler.EVENT_TEST_SUITE_START, EventHandler.EVENT_TEST_SUITE_END])
		this.seleniumServerRunner = seleniumServerRunner
		this.seleniumRunner = seleniumRunner
	}

	void onEvent(String event, Object... arguments) {
		String testType = arguments[0]
		switch (event) {
			case EVENT_TEST_SUITE_START:
				if (testType =~ /selenium/) {
					seleniumServerRunner.startServer()
					seleniumRunner.startSelenium()
				}
				break
			case EVENT_TEST_SUITE_END:
				if (testType =~ /selenium/) {
					seleniumRunner.stopSelenium()
					seleniumServerRunner.stopServer()
				}
				break
		}
	}
}
