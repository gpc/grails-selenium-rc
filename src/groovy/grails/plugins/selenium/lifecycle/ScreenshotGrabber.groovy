package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.SeleniumTestContextHolder
import grails.plugins.selenium.events.TestLifecycleListener
import org.slf4j.LoggerFactory
import org.apache.commons.lang.StringUtils
import com.thoughtworks.selenium.Selenium

class ScreenshotGrabber extends TestLifecycleListener {

	private final log = LoggerFactory.getLogger(ScreenshotGrabber)

	protected void onTestFailure(String testCaseName, String testName) {
		if (config.selenium.screenshot.onFail) {
			try {
				def path = generateScreenshotFilepath(testCaseName, testName)
				log.debug "Grabbing screenshot to $path"
				selenium.captureScreenshot(path)
			} catch (SeleniumException e) {
				log.error "Failed to capture screenshot", e
			}
		}
	}

	private String generateScreenshotFilepath(String testCaseName, String testName) {
		def directory = new File(config.selenium.screenshot.dir ?: "target/test-screenshots")
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

	private ConfigObject getConfig() {
		return SeleniumTestContextHolder.context.config
	}

	private Selenium getSelenium() {
		return SeleniumTestContextHolder.context.selenium
	}

}
