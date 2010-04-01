package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.SeleniumTestContext
import grails.plugins.selenium.events.EventHandlerSupport

class SeleniumRunner extends EventHandlerSupport {
	
	SeleniumRunner(SeleniumTestContext context) {
		super(context, [])
	}

	def void onEvent(String event, Object... arguments) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
