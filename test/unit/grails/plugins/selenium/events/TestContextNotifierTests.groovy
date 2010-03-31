package grails.plugins.selenium.events

import com.thoughtworks.selenium.Selenium
import grails.plugins.selenium.SeleniumTestContext
import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_CASE_START
import static grails.plugins.selenium.events.EventHandler.EVENT_TEST_START

@WithGMock
class TestContextNotifierTests {

	Selenium selenium
	SeleniumTestContext context
	TestContextNotifier notifier

	@Before
	void setUp() {
		selenium = mock(Selenium)
		context = mock(SeleniumTestContext)
		context.selenium.returns(selenium).stub()
		notifier = new TestContextNotifier(context)
	}

	@Test void updatesSeleniumWithTestNames() {
		selenium.context.set("TestCase1.test1")
		selenium.context.set("TestCase1.test2")
		selenium.context.set("TestCase2.test1")
		play {
			notifier.onEvent(EVENT_TEST_CASE_START, "TestCase1")
			notifier.onEvent(EVENT_TEST_START, "test1")
			notifier.onEvent(EVENT_TEST_START, "test2")
			notifier.onEvent(EVENT_TEST_CASE_START, "TestCase2")
			notifier.onEvent(EVENT_TEST_START, "test1")
		}
	}

	@Test void removesPackageNameFromTestCaseName() {
		selenium.context.set("TestCase1.test1")
		play {
			notifier.onEvent(EVENT_TEST_CASE_START, "com.whatever.project.TestCase1")
			notifier.onEvent(EVENT_TEST_START, "test1")
		}
	}
}
