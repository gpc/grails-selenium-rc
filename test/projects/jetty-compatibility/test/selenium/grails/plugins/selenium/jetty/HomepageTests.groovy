package grails.plugins.selenium.jetty

import grails.plugins.selenium.SeleniumTest
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import static org.junit.matchers.JUnitMatchers.hasItem

@Mixin(SeleniumTest)
class HomepageTests {

	@Test
	void loadsHomepage() {
		selenium.open "/"

		assertThat "page title", selenium.title, equalTo("Welcome to Grails")
	}

	@Test
	void runningOnJetty() {
		selenium.open "/"

		def plugins = []
		int index = 1
		while (selenium.isElementPresent("//div[@class='panelBody']/ul[2]/li[$index]")) {
			plugins << selenium.getText("//div[@class='panelBody']/ul[2]/li[$index]")
			index++
		}

		assertThat "plugin list", plugins, hasItem("jetty - 1.2-SNAPSHOT")
	}

}
