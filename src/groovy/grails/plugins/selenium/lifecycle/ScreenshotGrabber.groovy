package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.SeleniumTestContextHolder
import grails.plugins.selenium.events.TestLifecycleListener
import org.slf4j.LoggerFactory
import org.apache.commons.lang.StringUtils

class ScreenshotGrabber extends TestLifecycleListener {

	private final log = LoggerFactory.getLogger(ScreenshotGrabber)

	protected void onTestFailure(String testCaseName, String testName) {
		if (SeleniumTestContextHolder.context.config.selenium.screenshot.onFail) {
			try {
				log.warn "Taking screenshot"
				SeleniumTestContextHolder.context.selenium.captureScreenshot generateScreenshotFilepath(testCaseName, testName)
			} catch (SeleniumException e) {
				log.error "Failed to capture screenshot", e
			}
		}
	}

	private String generateScreenshotFilepath(String testCaseName, String testName) {
		def directory = new File("target/test-screenshots")
		def filename = "${removePackageName(testCaseName)}.${testName}.png"
		return new File(directory, filename).canonicalPath
	}

	String removePackageName(String testCaseName) {
		if (testCaseName.contains(".")) {
			return StringUtils.substringAfterLast(testCaseName, ".")
		} else {
			return testCaseName
		}
	}
}
