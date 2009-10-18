package grails.plugins.selenium

import org.gmock.WithGMock

@WithGMock
class GrailsSeleniumTests extends GroovyTestCase {

	GrailsSelenium selenium

	@Override void setUp() {
		super.setUp()
		selenium = new GrailsSelenium(null)
	}

	void testAndWaitAddedToNoArgMethods() {
		mock(selenium).refresh()
		mock(selenium).waitForPageToLoad("60000")
		play {
			selenium.refreshAndWait()
		}
	}

	void testAndWaitAddedToOneArgMethods() {
		mock(selenium).click("id=button")
		mock(selenium).waitForPageToLoad("60000")
		play {
			selenium.clickAndWait("id=button")
		}
	}

	void testAndWaitAddedToTwoArgMethods() {
		mock(selenium).fireEvent("id=element", "blur")
		mock(selenium).waitForPageToLoad("60000")
		play {
			selenium.fireEventAndWait("id=element", "blur")
		}
	}

	void testAndWaitUsesDefaultTimeout() {
		mock(selenium).click("id=button")
		mock(selenium).waitForPageToLoad("500")
		play {
			selenium.defaultTimeout = 500
			selenium.clickAndWait("id=button")
		}
	}

	void testMissingMethodExceptionThrownProperly() {
		shouldFail(MissingMethodException) {
			selenium.noSuchMethod()
		}
	}

}
