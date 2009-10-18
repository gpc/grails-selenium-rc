package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleneseTestCase

class TabsTests extends GrailsSeleneseTestCase {

	void testFirstTabIsInitiallySelected() {
		selenium.open "$rootURL/tabs.gsp"
		assertTabSelected 1
		assertVisible "tabs-1"
	}

	void testTabSelection() {
		selenium.with {
			open "$rootURL/tabs.gsp"
			[2, 3, 1].each {i ->
				click "//div[@id='tabs']/ul/li[$i]/a"
				waitForVisible "tabs-$i"
				assertTabSelected i
			}
		}
	}

	private void assertTabSelected(int i) {
		assertAttribute(/regex:\bui-tabs-selected\b/, "//div[@id='tabs']/ul/li[$i]@class")
	}

}