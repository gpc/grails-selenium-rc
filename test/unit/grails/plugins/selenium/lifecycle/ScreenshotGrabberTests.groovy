package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.SeleniumTestContext
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.anything
import static grails.plugins.selenium.events.EventHandler.*
import grails.plugins.selenium.lifecycle.ScreenshotGrabber

@WithGMock
class ScreenshotGrabberTests {

	Selenium selenium
	SeleniumTestContext context
	ScreenshotGrabber screenshotGrabber

	@Before
	void setUp() {
		selenium = mock(Selenium)

		context = mock(SeleniumTestContext)
		context.selenium.returns(selenium).stub()

		screenshotGrabber = new ScreenshotGrabber(context)
	}

	@Test
	void capturesScreenshotOnTestFailureEvent() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = true")
		context.config.returns(config).stub()
		selenium.captureScreenshot "WhateverTests.testWhatever.png"
		play {
			screenshotGrabber.onEvent(EVENT_TEST_CASE_START, "WhateverTests")
			screenshotGrabber.onEvent(EVENT_TEST_FAILURE, "testWhatever")
		}
	}

	@Test
	void doesNotCaptureScreenshotIfDisabledInConfig() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = false")
		context.config.returns(config).stub()
		play {
			screenshotGrabber.onEvent(EVENT_TEST_CASE_START, "WhateverTests")
			screenshotGrabber.onEvent(EVENT_TEST_FAILURE, "testWhatever")
		}
	}

	@Test
	void handlesExceptionsThrownWhenCapturingScreen() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = true")
		context.config.returns(config).stub()
		selenium.captureScreenshot(anything()).raises(new SeleniumException("screenshot failed"))
		play {
			screenshotGrabber.onEvent(EVENT_TEST_CASE_START, "WhateverTests")
			screenshotGrabber.onEvent(EVENT_TEST_FAILURE, "testWhatever")
		}
	}

}
