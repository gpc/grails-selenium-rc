package grails.plugins.selenium.events

import grails.plugins.selenium.SeleniumTestContext
import com.thoughtworks.selenium.SeleniumException
import org.slf4j.LoggerFactory

class ScreenshotGrabber extends TestCaseMonitor {

	private final log = LoggerFactory.getLogger(ScreenshotGrabber)
	private final SeleniumTestContext context

	ScreenshotGrabber(SeleniumTestContext context) {
		this.context = context
	}

	boolean handles(String event) {
		event == EVENT_TEST_FAILURE || super.handles(event)
	}

	void onEvent(String event, Object... arguments) {
		if (event == EVENT_TEST_FAILURE) {
			if (context.config.selenium.screenshot.onFail) {
				String testName = arguments[0]
				try {
					context.selenium.captureScreenshot "${currentTestCase}.${testName}.png"
				} catch (SeleniumException e) {
					log.error "Failed to capture screenshot", e
				}
			}
		} else {
			super.onEvent(event, arguments)
		}
	}
}
