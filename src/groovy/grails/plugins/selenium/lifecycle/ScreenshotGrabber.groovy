package grails.plugins.selenium.lifecycle

import grails.plugins.selenium.SeleniumTestContext
import com.thoughtworks.selenium.SeleniumException
import org.slf4j.LoggerFactory
import grails.plugins.selenium.events.TestCaseMonitor
import grails.plugins.selenium.events.EventHandler

class ScreenshotGrabber extends TestCaseMonitor {

	private final log = LoggerFactory.getLogger(ScreenshotGrabber)
	private final SeleniumTestContext context

	ScreenshotGrabber(SeleniumTestContext context) {
		super(EventHandler.EVENT_TEST_FAILURE)
		this.context = context
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
