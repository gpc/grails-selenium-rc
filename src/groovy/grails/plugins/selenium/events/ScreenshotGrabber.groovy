package grails.plugins.selenium.events

import grails.plugins.selenium.SeleniumTestContext
import com.thoughtworks.selenium.SeleniumException
import org.slf4j.LoggerFactory

class ScreenshotGrabber implements EventHandler {

	private final log = LoggerFactory.getLogger(ScreenshotGrabber)
	private final SeleniumTestContext context

	ScreenshotGrabber(SeleniumTestContext context) {
		this.context = context
	}

	boolean handles(String event) {
		event == EVENT_TEST_FAILURE
	}

	void onEvent(String event, Object... arguments) {
		if (context.screenshotOnFail()) {
			String testName = arguments[0]
			try {
				context.selenium.captureScreenshot "${context.currentTestCase}.${testName}.png"
			} catch (SeleniumException e) {
				log.error "Failed to capture screenshot", e
			}
		}
	}

}
