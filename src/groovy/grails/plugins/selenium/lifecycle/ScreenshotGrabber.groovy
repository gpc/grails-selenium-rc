package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.SeleniumTestContextHolder
import grails.plugins.selenium.events.TestLifecycleListener
import org.slf4j.LoggerFactory
import static grails.util.GrailsNameUtils.getShortName

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
		def filename = "${getShortName(testCaseName)}.${testName}.png"
		return new File(directory, filename).canonicalPath
	}

	private ConfigObject getConfig() {
		return SeleniumTestContextHolder.context.config
	}

	private Selenium getSelenium() {
		return SeleniumTestContextHolder.context.selenium
	}

}
