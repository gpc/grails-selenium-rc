package grails.plugins.selenium.events

abstract class EventHandlerSupport implements EventHandler {

	private final Collection<String> handledEvents

	EventHandlerSupport(Collection<String> handledEvents) {
		this.handledEvents = new HashSet<String>(handledEvents).asImmutable()
	}

	EventHandlerSupport(String handledEvent) {
		this.handledEvents = Collections.singleton(handledEvent)
	}

	final boolean handles(String event) {
		event in handledEvents
	}

}
