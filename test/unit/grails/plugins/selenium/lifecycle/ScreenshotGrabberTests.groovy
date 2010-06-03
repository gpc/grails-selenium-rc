/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.SeleniumWrapper
import org.gmock.WithGMock
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.anything
import static org.hamcrest.CoreMatchers.equalTo

@WithGMock
class ScreenshotGrabberTests {

	SeleniumWrapper selenium

	@Before
	void setUp() {
		selenium = mock(SeleniumWrapper)
	}

	@Test
	void capturesScreenshotOnTestFailureEvent() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = true")
		def screenshotGrabber = new ScreenshotGrabber(selenium, config)

		selenium.captureScreenshot pathTo(new File("target/test-reports/test-screenshots/WhateverTests.testWhatever.png"))

		play {
			screenshotGrabber.onTestFailure("some.package.WhateverTests", "testWhatever")
		}
	}

	@Test
	void usesConfiguredDirectory() {
		def config = new ConfigSlurper().parse("""
			selenium.screenshot.onFail = true
			selenium.screenshot.dir = "some/directory/path"
		""")
		def screenshotGrabber = new ScreenshotGrabber(selenium, config)

		selenium.captureScreenshot pathTo(new File("some/directory/path/WhateverTests.testWhatever.png"))

		play {
			screenshotGrabber.onTestFailure("some.package.WhateverTests", "testWhatever")
		}
	}

	@Test
	void doesNotCaptureScreenshotIfDisabledInConfig() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = false")
		def screenshotGrabber = new ScreenshotGrabber(selenium, config)

		play {
			screenshotGrabber.onTestFailure("WhateverTests", "testWhatever")
		}
	}

	@Test
	void handlesExceptionsThrownWhenCapturingScreen() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = true")
		def screenshotGrabber = new ScreenshotGrabber(selenium, config)

		selenium.captureScreenshot(anything()).raises(new SeleniumException("screenshot failed"))
		play {
			screenshotGrabber.onTestFailure("WhateverTests", "testWhatever")
		}
	}

	static Matcher<String> pathTo(File expectedFile) {
		equalTo(expectedFile.canonicalPath)
	}

}
