package grails.plugins.selenium.test

import grails.plugins.selenium.*

@Mixin(SeleniumTest)
class HomepageSpecification extends Specification {

	def "user can load application homepage"() {
		when:
		selenium.open(contextPath)

		then:
		selenium.isTextPresent("Welcome to Grails")
	}

}