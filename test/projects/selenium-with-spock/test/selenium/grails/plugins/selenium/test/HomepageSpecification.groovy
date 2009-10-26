package grails.plugins.selenium.test

import spock.lang.*
import grails.plugin.spock.*
import grails.plugins.selenium.*

@Mixin(SeleneseTestCategory)
class HomepageSpecification extends Specification {

	def "user can load application homepage"() {
		when:
		selenium.open(contextPath)

		then:
		selenium.isTextPresent("Welcome to Grails")
	}

}