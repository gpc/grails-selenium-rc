package grails.plugins.selenium.test.spock

import grails.plugins.selenium.*
import spock.lang.Specification

@Mixin (SeleniumTest)
class HomepageSpecification extends Specification {

	def "user can load application homepage"() {
		when:
		selenium.open "/"

		then:
		selenium.isTextPresent "Welcome to Grails"
	}

}