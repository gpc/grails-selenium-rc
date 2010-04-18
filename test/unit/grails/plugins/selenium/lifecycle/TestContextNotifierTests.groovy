package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumTestContext
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_CASE_START
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_START
import grails.plugins.selenium.lifecycle.TestContextNotifier
import grails.plugins.selenium.DefaultSeleniumTestContext
import grails.plugins.selenium.SeleniumTestContextHolder

@WithGMock
class TestContextNotifierTests {

	Selenium selenium
	TestContextNotifier notifier

	@Before
	void setUp() {
		selenium = mock(Selenium)

		SeleniumTestContextHolder.context = new DefaultSeleniumTestContext(selenium, null)

		notifier = new TestContextNotifier()
	}

	@Test void updatesSeleniumWithTestNames() {
		selenium.context.set("TestCase1.test1")
		selenium.context.set("TestCase1.test2")
		selenium.context.set("TestCase2.test1")
		play {
			notifier.receiveGrailsBuildEvent(EVENT_TEST_CASE_START, "TestCase1")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_START, "test1")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_START, "test2")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_CASE_START, "TestCase2")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_START, "test1")
		}
	}

	@Test void removesPackageNameFromTestCaseName() {
		selenium.context.set("TestCase1.test1")
		play {
			notifier.receiveGrailsBuildEvent(EVENT_TEST_CASE_START, "com.whatever.project.TestCase1")
			notifier.receiveGrailsBuildEvent(EVENT_TEST_START, "test1")
		}
	}
}
