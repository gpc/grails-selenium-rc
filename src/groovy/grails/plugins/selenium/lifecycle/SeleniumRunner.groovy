package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.events.EventHandler

class SeleniumRunner implements EventHandler {

	def boolean handles(String event) {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	def void onEvent(String event, Object... arguments) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
