# Using Other Test Frameworks
Selenium tests do not have to be written as JUnit test cases. The `@SeleniumAware` mixin could be applied to almost any test case written in another framework. Page objects are also agnostic of the test framework.

Currently the plugin has tested support for [Spock][1]. Also, when using Grails 1.3+ tests can be written using JUnit 4 conventions. Other testing frameworks such as [EasyB][2] and [JBehave][3], may well work already and support is planned in future versions of the Selenium plugin.

## Writing Selenium tests with JUnit 4
Grails 1.3+ uses JUnit 4 by default and although you can carry on writing tests as usual JUnit 4 provides some nice enhancements such as the `assertThat` method that works with [Hamcrest matchers][4].

	import org.junit.*
	import grails.plugins.selenium.*
	import static org.junit.Assert.*
	import static org.hamcrest.CoreMatchers.*

	@Mixin(SeleniumAware)
	class HomepageTests {
		@Test void userCanLoadHomepage() {
			selenium.open "/"
			assertThat selenium.title, equalTo("Welcome to Grails")
		}
	}
	
Note the test does not extend any base class and annotates each test method with `@Test`.

## Writing Selenium tests with Spock
To write Selenium tests using Spock, simply create your specification under `test/selenium` and (optionally) use the `@SeleniumAware` mixin then run tests as normal. For example:

	import spock.lang.*
	import grails.plugins.selenium.*

	@Mixin(SeleniumAware)
	class HomepageSpecification extends Specification {
	    def "user can load application homepage"() {
	        when:
	            selenium.open("/")
	        then:
	            selenium.isTextPresent("Welcome to Grails")
	    }
	}

You can isolate Spock Selenium tests with `grails test-app :spock-selenium` or `grails test-app functional:spock-selenium` just as you can for other test types.

[1]: http://grails.org/plugin/spock "Spock Grails plugin"
[2]: http://www.easyb.org/ "EasyB BDD framework"
[3]: http://jbehave.org/ "JBehave BDD framework"
[4]: http://code.google.com/p/hamcrest/ "Hamcrest matcher library"
