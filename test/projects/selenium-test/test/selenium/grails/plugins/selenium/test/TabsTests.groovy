package grails.plugins.selenium.test

import static grails.plugins.selenium.condition.ClosureEvaluatingWait.waitFor

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
			waitFor("Timed out waiting for tabs-$i to become visible") {
				selenium.isVisible("tabs-$i")
			}
			assertTabSelected i
		}
	}

}