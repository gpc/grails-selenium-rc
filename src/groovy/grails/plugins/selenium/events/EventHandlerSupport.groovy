package grails.plugins.selenium.events

import grails.plugins.selenium.SeleniumTestContext

abstract class EventHandlerSupport implements EventHandler {

	private final SeleniumTestContext context
	private final Collection<String> handledEvents

	EventHandlerSupport(SeleniumTestContext context, Collection<String> handledEvents) {
		this.context = context
		this.handledEvents = new HashSet<String>(handledEvents).asImmutable()
	}

	EventHandlerSupport(SeleniumTestContext context, String handledEvent) {
		this(context, Collections.singleton(handledEvent))
	}

	protected final SeleniumTestContext getContext() { context }

	final boolean handles(String event) {
		event in handledEvents
	}

}
