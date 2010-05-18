package grails.plugin.selenium.tests

import grails.plugins.selenium.SeleniumAware
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo

@Mixin(SeleniumAware)
class PageOpeningTests extends GroovyTestCase {
	
	void testCanOpenPageAndVerifyContents() {
		selenium.open "/"
		assertThat selenium.title, equalTo("Welcome to Grails")
	}

	void testCanNavigateBetweenPages() {
		selenium.open "/"
		assertThat selenium.title, equalTo("Welcome to Grails")

		selenium.clickAndWait("link=musicstore.ArtistController")
		assertThat selenium.title, equalTo("Artist List")

		selenium.clickAndWait("css=.nav a.home")
		assertThat selenium.title, equalTo("Welcome to Grails")
	}
	
	void testCanUseGoBack() {
		selenium.open "/"
		assertThat selenium.title, equalTo("Welcome to Grails")

		selenium.clickAndWait("link=musicstore.ArtistController")
		assertThat selenium.title, equalTo("Artist List")

		selenium.goBack()
		assertThat selenium.title, equalTo("Welcome to Grails")
	}

	void testCanUseWaitForPageToLoadWithNoArgs() {
		selenium.open "/"

		selenium.click("link=musicstore.ArtistController")
		selenium.waitForPageToLoad()

		assertThat selenium.title, equalTo("Artist List")
	}

}