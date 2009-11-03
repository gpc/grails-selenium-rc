package grails.plugins.selenium

import org.gmock.WithGMock
import grails.test.GrailsUnitTestCase
import junit.framework.AssertionFailedError

@WithGMock
class SeleniumTestTests extends GrailsUnitTestCase {

	def testCase

	void setUp() {
		super.setUp()
		testCase = new TestCaseImpl()
	}

	void tearDown() {
		super.tearDown()
		SeleniumManager.instance.selenium = null
		SeleniumManager.instance.config = null
	}

	void testSeleniumInstanceIsAvailable() {
		def seleniumMock = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = seleniumMock
		seleniumMock.open("/")
		play {
			testCase.testOpenPage()
		}
	}

	void testConfigIsAvailable() {
		SeleniumManager.instance.config = new ConfigSlurper().parse("selenium.browser = '*firefox'")
		assertEquals "*firefox", testCase.config.selenium.browser
	}

	void testRootUrlIsAvailable() {
		mockConfig "web.app.context.path = 'foo'"
		assertEquals "/foo", testCase.contextPath
	}

	void testWaitForSuccess() {
		def seleniumMock = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = seleniumMock
		seleniumMock.getDefaultTimeout().returns(1000)
		play {
			testCase.waitFor {
				true
			}
		}
	}

	void testWaitForFailure() {
		def seleniumMock = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = seleniumMock
		seleniumMock.getDefaultTimeout().returns(1000)
		play {
			def message = shouldFail(AssertionFailedError) {
				testCase.waitFor {
					false
				}
			}
			assertEquals "Timed out.", message
		}
	}

	void testWaitForFailureWithMessage() {
		def seleniumMock = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = seleniumMock
		seleniumMock.getDefaultTimeout().returns(1000)
		play {
			def message = shouldFail(AssertionFailedError) {
				testCase.waitFor("something to happen") {
					false
				}
			}
			assertEquals "Timed out waiting for: something to happen.", message
		}
	}
	
}

@Mixin(SeleniumTest)
class TestCaseImpl {

	void testOpenPage() {
		selenium.open("/")
	}

}
