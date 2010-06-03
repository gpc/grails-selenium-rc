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
