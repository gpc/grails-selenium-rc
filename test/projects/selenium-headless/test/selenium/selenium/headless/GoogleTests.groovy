package selenium.headless

import grails.plugins.selenium.SeleniumTest

@Mixin(SeleniumTest)
class GoogleTests extends GroovyTestCase {

	void testHitRemoteServerWithoutStartingGrails() {
		selenium.open("http://google.com/")
	}

}