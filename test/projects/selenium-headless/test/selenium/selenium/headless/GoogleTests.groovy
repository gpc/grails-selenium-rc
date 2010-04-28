package selenium.headless

import grails.plugins.selenium.SeleniumAware

@Mixin(SeleniumAware)
class GoogleTests extends GroovyTestCase {

	void testHitRemoteServerWithoutStartingGrails() {
		selenium.open "/"
		assertEquals "Google", selenium.title

		selenium.type "css=form input[name=q]", "Grails"
		selenium.clickAndWait "css=form input[name=btnG]"
		assertEquals "Grails - Google Search", selenium.title
	}

}
