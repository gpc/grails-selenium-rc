package scenarios

import org.jbehave.scenario.*
import org.jbehave.scenario.annotations.*
import org.jbehave.scenario.steps.*
import grails.plugins.selenium.*
import com.thoughtworks.selenium.Selenium

@Mixin(SeleniumAware)
class HomepageSteps extends Steps {

	@Given('I am not logged in')
	void logout() {
		// does nothing right now
	}

	@When('I visit the homepage')
	void openHomepage() {
		selenium.open "/"
	}

	@Then('I should see a message, "$message"')
	void verifyMessage(String message) {
		assert selenium.isTextPresent(message)
	}
}
