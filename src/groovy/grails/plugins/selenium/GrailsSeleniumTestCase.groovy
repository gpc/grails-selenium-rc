package grails.plugins.selenium

import com.thoughtworks.selenium.SeleneseTestBase
import com.thoughtworks.selenium.Selenium
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * An adaptation of GroovySeleneseTestCase that expects a Selenium instance to be injected
 * by the test runner rather than creating its own. This means an entire suite of tests can
 * be run in a single browser session.
 */
@Mixin(SeleniumAware)
@Deprecated
class GrailsSeleniumTestCase extends GroovyTestCase {

	@Delegate SeleneseTestBase base = new SeleneseTestBase()

	@Override
	void tearDown() {
		super.tearDown()
		checkForVerificationErrors()
	}

	/**
	 * Returns the URL context path for the application. This is required to prefix URLs, e.g. to have
	 * Selenium open the project root you would use: selenium.open("$contextPath/")
	 */
	String getContextPath() {
		def appContext = ConfigurationHolder.config.app.context ?: ApplicationHolder.application.metadata."app.name"
		if (!appContext.startsWith("/")) appContext = "/$appContext"
		return appContext
	}

	/**
	 * Delegates missing method calls to the SeleneseTestBase object where
	 * possible. Also enables direct assert, verify and waitFor calls to
	 * be made on the running Selenium instance. Selenium.get* and Selenium.is*
	 * can be invoked as e.g. assertTextPresent, verifyAttribute, waitForVisible,
	 * etc.
	 */
	def methodMissing(String name, args) {
		boolean handled = false

		def match = name =~ /^(assert|verify|waitFor)(Not)?(.+)$/
		if (match.find()) {
			def condition = match[0][1]
			boolean negated = match[0][2] == "Not"
			def command = match[0][3]

			def result
			if (Selenium.metaClass.respondsTo(selenium, "is$command")) {
				handled = true
				switch (condition) {
					case "assert":
						SeleneseTestBase."assert${negated ? 'False' : 'True'}" selenium."is$command"(* args)
						break
					case "verify":
						base."verify${negated ? 'False' : 'True'}" selenium."is$command"(* args)
						break
					case "waitFor":
						selenium."$name"(* args)
						break
				}
			} else if (Selenium.metaClass.respondsTo(selenium, "get$command")) {
				handled = true
				def expected = args[0]
				def seleniumArgs = args.length > 1 ? args[1..-1] : [] as Object[]
				switch (condition) {
					case "assert":
						SeleneseTestBase."assert${negated ? 'Not' : ''}Equals" expected, selenium."get$command"(* seleniumArgs)
						break
					case "verify":
						base."verify${negated ? 'Not' : ''}Equals" expected, selenium."get$command"(* seleniumArgs)
						break
					case "waitFor":
						selenium."$name"(args[-1], * args[0..-2])
						break
				}
			}
		}

		if (!handled) throw new MissingMethodException(name, getClass(), args)
	}
}
