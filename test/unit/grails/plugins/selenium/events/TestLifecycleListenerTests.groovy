package grails.plugins.selenium.events

import org.junit.Test
import org.gmock.WithGMock
import org.junit.Before
import static grails.plugins.selenium.events.TestLifecycleListener.*
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*

@WithGMock
class TestLifecycleListenerTests {

	TestLifecycleListener listener

	@Before
	void setUp() {
		listener = new MockTestLifecycleListener()
	}

	@Test
	void passesTestCaseAndTestNameOnTestStartEvent() {
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
		mock(listener) {
			onTestFailure("TestCaseName", "testName")
		}
		play {
			listener.receiveGrailsBuildEvent EVENT_TEST_CASE_START, ["TestCaseName"] as Object[]
			listener.receiveGrailsBuildEvent EVENT_TEST_FAILURE, ["testName"] as Object[]
		}
	}

}

class MockTestLifecycleListener extends TestLifecycleListener { }
