package grails.plugins.selenium.events

import grails.plugins.selenium.SeleniumTestContext
import grails.plugins.selenium.SeleniumTestContextHolder
import org.gmock.WithGMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.TestLifecycleListener.*
import static org.hamcrest.CoreMatchers.anything

@WithGMock
class TestLifecycleListenerTests {

	TestLifecycleListener listener

	@Before
	void setUp() {
		listener = new MockTestLifecycleListener()
	}

	@After
	void tearDown() {
		SeleniumTestContextHolder.context = null		
	}

	@Test
	void passesTestCaseAndTestNameOnTestStartEvent() {
		mock(listener) {
			onTestStart("TestCaseName", "testName")
		}
		play {
			SeleniumTestContextHolder.context = new SeleniumTestContext(null, null)
			listener.receiveGrailsBuildEvent EVENT_TEST_CASE_START, ["TestCaseName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_START, ["testName"] as Object[]
		}
	}

	@Test
	void passesTestCaseAndTestNameOnTestFailureEvent() {
		mock(listener) {
			onTestFailure("TestCaseName", "testName")
		}
		play {
			SeleniumTestContextHolder.context = new SeleniumTestContext(null, null)
			listener.receiveGrailsBuildEvent EVENT_TEST_CASE_START, ["TestCaseName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_FAILURE, ["testName"] as Object[]
		}
	}

	@Test
	void ignoresEventsIfNotRunningSeleniumTests() {
		mock(listener) {
			onTestStart(anything(), anything()).never()
			onTestFailure(anything(), anything()).never()
		}
		play {
			SeleniumTestContextHolder.context = null
			listener.receiveGrailsBuildEvent EVENT_TEST_CASE_START, ["TestCaseName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_START, ["testName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_FAILURE, ["testName"] as Object[]
		}
	}

}

class MockTestLifecycleListener extends TestLifecycleListener { }
