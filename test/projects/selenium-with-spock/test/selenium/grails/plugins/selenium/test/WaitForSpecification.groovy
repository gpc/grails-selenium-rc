package grails.plugins.selenium.test

import grails.plugins.selenium.*
import spock.lang.Specification

@Mixin(SeleniumAware)
class WaitForSpecification extends Specification {

	def "waitFor can be used in then block"() {
		given: "a user is on the home page"
		selenium.open "/"
		
		when: "they click on a link"
		selenium.click "link=grails.plugins.selenium.test.SongController"

		then: "the new page loads"
		selenium.waitForTitle "Song List"
	}

}
