package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.SeleniumTestContextHolder
import grails.plugins.selenium.events.TestLifecycleListener
import org.slf4j.LoggerFactory

class ScreenshotGrabber extends TestLifecycleListener {

	private final log = LoggerFactory.getLogger(ScreenshotGrabber)

	protected void onTestFailure(String testCaseName, String testName) {
		if (SeleniumTestContextHolder.context.config.selenium.screenshot.onFail) {
			try {
				SeleniumTestContextHolder.context.selenium.captureScreenshot "${testCaseName}.${testName}.png"
			} catch (SeleniumException e) {
				log.error "Failed to capture screenshot", e
			}
		}
	}
}
