package grails.plugins.selenium.test.extensions

import grails.plugins.selenium.SeleniumTest
import org.junit.Ignore
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat

@Mixin(SeleniumTest)
class UserExtensionsTests {

	@Ignore @Test
	void canUseSeleniumUserExtensions() {
		selenium.open "/"
		assertThat "login state", selenium.getText("css=.loginInfo"), equalTo("Not logged in")

		selenium.login "blackbeard", "password"

		assertThat "login state", selenium.getText("css=.loginInfo"), equalTo("Logged in as blackbeard")

	}

}
