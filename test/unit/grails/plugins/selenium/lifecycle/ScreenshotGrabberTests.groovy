package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.DefaultSeleniumTestContext
import grails.plugins.selenium.SeleniumTestContextHolder
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.BaseMatcher
import org.hamcrest.Description

@WithGMock
class ScreenshotGrabberTests {

	Selenium selenium
	ScreenshotGrabber screenshotGrabber

	@Before
	void setUp() {
		selenium = mock(Selenium)

		screenshotGrabber = new ScreenshotGrabber()
	}

	@Test
	void capturesScreenshotOnTestFailureEvent() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = true")
		SeleniumTestContextHolder.context = new DefaultSeleniumTestContext(selenium, config)
		selenium.captureScreenshot pathTo(new File("target/test-screenshots/WhateverTests.testWhatever.png"))
		play {
			screenshotGrabber.onTestFailure("some.package.WhateverTests", "testWhatever")
		}
	}

	@Test
	void doesNotCaptureScreenshotIfDisabledInConfig() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = false")
		SeleniumTestContextHolder.context = new DefaultSeleniumTestContext(selenium, config)
		play {
			screenshotGrabber.onTestFailure("WhateverTests", "testWhatever")
		}
	}

	@Test
	void handlesExceptionsThrownWhenCapturingScreen() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = true")
		SeleniumTestContextHolder.context = new DefaultSeleniumTestContext(selenium, config)
		selenium.captureScreenshot(anything()).raises(new SeleniumException("screenshot failed"))
		play {
			screenshotGrabber.onTestFailure("WhateverTests", "testWhatever")
		}
	}

	static Matcher<String> pathTo(File expectedFile) {
		equalTo(expectedFile.canonicalPath)
	}

}
