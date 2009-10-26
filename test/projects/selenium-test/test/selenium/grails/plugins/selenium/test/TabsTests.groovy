package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleneseTestCase

class TabsTests extends GrailsSeleneseTestCase {

	void testFirstTabIsInitiallySelected() {
		selenium.open "$contextPath/tabs.gsp"
		assertTabSelected 1
		assertVisible "tabs-1"
	}

	void testTabSelection() {
		selenium.open "$contextPath/tabs.gsp"
		[3, 2, 1].each {i ->
			selenium.click "//div[@id='tabs']/ul/li[$i]/a"
			waitForVisible "tabs-$i"
			assertTabSelected i
		}
	}

	private void assertTabSelected(int i) {
		assertAttribute(/regex:\bui-tabs-selected\b/, "//div[@id='tabs']/ul/li[$i]@class")
	}

}