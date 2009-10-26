package grails.plugins.selenium

import org.gmock.WithGMock
import grails.test.GrailsUnitTestCase

@WithGMock
class SeleneseTestCategoryTests extends GrailsUnitTestCase {

	def seleniumMock

	void setUp() {
		super.setUp()
		seleniumMock = mock(GrailsSelenium)
		SeleniumManager.instance.selenium = seleniumMock
	}

	void testSeleniumInstanceIsAvailable() {
		def testCase = new TestCaseImpl()
		seleniumMock.open("/")
		play {
			testCase.testOpenPage()
		}
	}

	void testConfigIsAvailable() {
		def testCase = new TestCaseImpl()
		SeleniumManager.instance.config = new ConfigSlurper().parse("selenium.browser = '*firefox'")
		assertEquals "*firefox", testCase.config.selenium.browser
	}

	void testRootUrlIsAvailable() {
		def testCase = new TestCaseImpl()
		mockConfig "web.app.context.path = 'foo'"
		assertEquals "/foo", testCase.rootURL
	}
	
}

@Mixin(SeleneseTestCategory)
class TestCaseImpl {

	void testOpenPage() {
		selenium.open("/")
	}

}
