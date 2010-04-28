package grails.plugins.selenium.test

class TabsTests extends AbstractTabsTestCase {

	void testFirstTabIsInitiallySelected() {
		selenium.open "/tabs.gsp"
		assertTabSelected 1
		assertTrue selenium.isVisible("tabs-1")
	}

	void testTabSelection() {
		selenium.open "/tabs.gsp"
		[3, 2, 1].each {i ->
			selenium.click "//div[@id='tabs']/ul/li[$i]/a"
			selenium.waitForVisible("tabs-$i")
			assertTabSelected i
		}
	}

}