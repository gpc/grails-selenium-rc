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

package grails.plugins.selenium.events

import com.thoughtworks.selenium.Selenium
import grails.build.GrailsBuildListener
import org.slf4j.LoggerFactory
import grails.plugins.selenium.SeleniumWrapper

abstract class TestLifecycleListener implements GrailsBuildListener {

	public static final String EVENT_TEST_FAILURE = "TestFailure"
	public static final String EVENT_TEST_START = "TestStart"
	public static final String EVENT_TEST_CASE_START = "TestCaseStart"

	private String currentTestCaseName
	protected SeleniumWrapper selenium

	protected final log = LoggerFactory.getLogger(getClass())

	TestLifecycleListener(SeleniumWrapper selenium) {
		this.selenium = selenium
	}

	void receiveGrailsBuildEvent(String eventName, Object... args) {
		if (!selenium.alive) return
		switch (eventName) {
			case EVENT_TEST_CASE_START:
				currentTestCaseName = args[0]
				break
			case EVENT_TEST_START:
				String testName = args[0]
				onTestStart(currentTestCaseName, testName)
				break
			case EVENT_TEST_FAILURE:
				String testName = args[0]
				onTestFailure(currentTestCaseName, testName)
				break
		}
	}

	protected void onTestStart(String testCaseName, String testName) { }

	protected void onTestFailure(String testCaseName, String testName) { }

}
