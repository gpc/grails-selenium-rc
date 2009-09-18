package selenium.test

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase

class TabsTests extends GrailsSeleneseTestCase {

	void testFirstTabIsInitiallySelected() {
		selenium.open "$rootURL/tabs.gsp"
		assertTabSelected 1
		assertVisible "tabs-1"
	}

	void testTabSelection() {
		selenium.open "$rootURL/tabs.gsp"
		[2, 3, 1].each { i->
			selenium.click "//div[@id='tabs']/ul/li[$i]/a"
			waitForVisible "tabs-$i"
			assertTabSelected i
		}
	}

	private void assertTabSelected(int i) {
		// TODO: should use assertAttribute "//div[@id='tabs']/ul/li[$i]@class", /regex:\bui-tabs-selected\b/ but delegation to SeleneseTestBase assertEquals doesn't work right
		assertNotNull selenium.getAttribute("//div[@id='tabs']/ul/li[$i]@class") ==~ /\bui-tabs-selected\b/
	}

}