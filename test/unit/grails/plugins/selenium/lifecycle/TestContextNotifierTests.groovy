package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.Selenium
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.TestLifecycleListener.EVENT_TEST_CASE_START
import static grails.plugins.selenium.events.TestLifecycleListener.EVENT_TEST_START
import grails.plugins.selenium.SeleniumWrapper

@WithGMock
class TestContextNotifierTests {

	SeleniumWrapper selenium
	TestContextNotifier notifier

	@Before
	void setUp() {
		selenium = mock(SeleniumWrapper) {
			isAlive().returns(true).stub()
		}
		notifier = new TestContextNotifier(selenium)
	}

	@Test
	void updatesSeleniumWithTestNames() {
		selenium.showContextualBanner("TestCase1", "test1")
		selenium.showContextualBanner("TestCase1", "test2")
		selenium.showContextualBanner("TestCase2", "test1")
		play {
			notifier.receiveGrailsBuildEvent(EVENT_TEST_CASE_START, "TestCase1")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_START, "test1")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_START, "test2")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_CASE_START, "TestCase2")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_START, "test1")
		}
	}

	@Test
	void removesPackageNameFromTestCaseName() {
		selenium.showContextualBanner("TestCase1", "test1")
		play {
			notifier.receiveGrailsBuildEvent(EVENT_TEST_CASE_START, "com.whatever.project.TestCase1")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_START, "test1")
		}
	}
}
