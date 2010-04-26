package grails.plugins.selenium.test.extensions

import grails.plugins.selenium.SeleniumTest
import org.junit.Ignore
import org.junit.Test
import static org.hamcrest.CoreMatchers.equalTo
import static org.junit.Assert.assertThat
import com.thoughtworks.selenium.HttpCommandProcessor

@Mixin(SeleniumTest)
class UserExtensionsTests {

	@Ignore @Test
	void canUseSeleniumUserExtensions() {
		selenium.open "/"
		assertThat "login state", selenium.getText("css=.loginInfo"), equalTo("Not logged in")

//		selenium.login "blackbeard", "password"
		HttpCommandProcessor proc = selenium.commandProcessor
		proc.doCommand("login", ["blackbeard", "password"] as String)

		assertThat "login state", selenium.getText("css=.loginInfo"), equalTo("Logged in as blackbeard")

	}

}
