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

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.TestLifecycleListener.*
import static org.hamcrest.CoreMatchers.anything
import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumWrapper

@WithGMock
class TestLifecycleListenerTests {

	SeleniumWrapper selenium
	TestLifecycleListener listener

	@Before
	void setUp() {
		selenium = mock(SeleniumWrapper)
		listener = new MockTestLifecycleListener(selenium)
	}

	@Test
	void passesTestCaseAndTestNameOnTestStartEvent() {
		selenium.isAlive().returns(true).stub()
		mock(listener) {
			onTestStart("TestCaseName", "testName")
		}
		play {
			listener.receiveGrailsBuildEvent EVENT_TEST_CASE_START, ["TestCaseName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_START, ["testName"] as Object[]
		}
	}

	@Test
	void passesTestCaseAndTestNameOnTestFailureEvent() {
		selenium.isAlive().returns(true).stub()
		mock(listener) {
			onTestFailure("TestCaseName", "testName")
		}
		play {
			listener.receiveGrailsBuildEvent EVENT_TEST_CASE_START, ["TestCaseName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_FAILURE, ["testName"] as Object[]
		}
	}

	@Test
	void ignoresEventsIfNotRunningSeleniumTests() {
		selenium.isAlive().returns(false).stub()
		mock(listener) {
			onTestStart(anything(), anything()).never()
			onTestFailure(anything(), anything()).never()
		}
		play {
			listener.receiveGrailsBuildEvent EVENT_TEST_CASE_START, ["TestCaseName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_START, ["testName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_FAILURE, ["testName"] as Object[]
		}
	}

}

class MockTestLifecycleListener extends TestLifecycleListener {
	MockTestLifecycleListener(SeleniumWrapper selenium) {
		super(selenium)
	}
}
