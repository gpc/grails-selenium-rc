package grails.plugins.selenium.test

import spock.lang.*
import grails.plugins.selenium.*

@Mixin(Selenese)
class HomepageSpecification extends Specification {

	def "user can load application homepage"() {
		when:
		selenium.open(contextPath)

		then:
		selenium.isTextPresent("Welcome to Grails")
	}

}