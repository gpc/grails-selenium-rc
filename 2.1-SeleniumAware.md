# The `@SeleniumAware` Mixin
The simplest way to write Selenium tests is to create a class that extends `GroovyTestCase` and add the `SeleniumAware` mixin that the plugin provides. The mixin class simply makes the running instance of `Selenium` available:

	import grails.plugins.selenium.SeleniumAware

	@Mixin(SeleniumAware)
	class HomepageTests extends GroovyTestCase {
	    void testHomepageLoads() {
	        selenium.open "/"
	        assertTrue selenium.isTextPresent("Welcome to Grails")
	    }
	}

References to the `selenium` property in the test will get the running `Selenium` instance from the mixin class.
