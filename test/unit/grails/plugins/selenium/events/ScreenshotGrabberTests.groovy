package grails.plugins.selenium.events

import com.thoughtworks.selenium.Selenium
import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.SeleniumTestContext
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.anything
import static grails.plugins.selenium.events.EventHandler.*

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
		context.currentTestCase.returns("WhateverTests").stub()
		screenshotGrabber = new ScreenshotGrabber(context)
	}

	@Test
	void capturesScreenshotOnTestFailureEvent() {
		context.screenshotOnFail().returns(true).stub()
		selenium.captureScreenshot "WhateverTests.testWhatever.png"
		play {
			screenshotGrabber.onEvent(EVENT_TEST_FAILURE, "testWhatever")
		}
	}

	@Test
	void doesNotCaptureScreenshotIfDisabledInConfig() {
		context.screenshotOnFail().returns(false).stub()
		play {
			screenshotGrabber.onEvent(EVENT_TEST_FAILURE, "testWhatever")
		}
	}

	@Test
	void handlesExceptionsThrownWhenCapturingScreen() {
		context.screenshotOnFail().returns(true).stub()
		selenium.captureScreenshot(anything()).raises(new SeleniumException("screenshot failed"))
		play {
			screenshotGrabber.onEvent(EVENT_TEST_FAILURE, "testWhatever")
		}
	}

}
