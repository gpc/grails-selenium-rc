package grails.plugins.selenium.test

class HomepageSpecification extends SeleneseSpecification {

	def "user can load application homepage"() {
		when:
		selenium.open(rootURL)

		then:
		selenium.isTextPresent("Welcome to Grails")
	}

}