package grails.plugins.selenium.test

import grails.plugins.selenium.GrailsSeleneseTestCase
import grails.plugins.selenium.SeleneseTestCategory

@Mixin(SeleneseTestCategory)
class TabsTests extends GroovyTestCase {

	void testFirstTabIsInitiallySelected() {
		selenium.open "$contextPath/tabs.gsp"
		assertTabSelected 1
		assertTrue selenium.isVisible("tabs-1")
	}

	void testTabSelection() {
		selenium.open "$contextPath/tabs.gsp"
		[3, 2, 1].each {i ->
			selenium.click "//div[@id='tabs']/ul/li[$i]/a"
			waitFor {
				selenium.isVisible("tabs-$i")
			}
			assertTabSelected i
		}
	}

	private void assertTabSelected(int i) {
		assertNotNull selenium.getAttribute("//div[@id='tabs']/ul/li[$i]@class") =~ /\bui-tabs-selected\b/
	}

}