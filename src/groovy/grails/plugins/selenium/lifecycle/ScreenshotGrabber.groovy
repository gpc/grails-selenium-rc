package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.events.TestLifecycleListener
import static grails.util.GrailsNameUtils.getShortName
import grails.plugins.selenium.SeleniumWrapper

class ScreenshotGrabber extends TestLifecycleListener {

	static final DEFAULT_DIRECTORY = "target/test-reports/test-screenshots"

	private final File screenshotDirectory
	private final boolean enabled

	ScreenshotGrabber(SeleniumWrapper selenium, ConfigObject config) {
		super(selenium)
		screenshotDirectory = new File(config.selenium.screenshot.dir ?: DEFAULT_DIRECTORY)
		enabled = config.selenium.screenshot.onFail
	}

	protected void onTestFailure(String testCaseName, String testName) {
		if (enabled) {
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
		def filename = "${getShortName(testCaseName)}.${testName}.png"
		return new File(screenshotDirectory, filename).canonicalPath
	}
}
