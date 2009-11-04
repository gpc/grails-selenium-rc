package grails.plugins.selenium

import com.thoughtworks.selenium.SeleneseTestBase
import com.thoughtworks.selenium.Selenium

/**
 * An adaptation of GroovySeleneseTestCase that expects a Selenium instance to be injected
 * by the test runner rather than creating its own. This means an entire suite of tests can
 * be run in a single browser session.
 */
@Mixin(SeleniumTest)
class GrailsSeleniumTestCase extends GroovyTestCase {

	@Delegate private final SeleneseTestBase base = new SeleneseTestBase()

	@Override
	void setUp() {
		super.setUp()
		selenium.setContext("${getClass().simpleName}.$name")
	}

	@Override
	void tearDown() {
		super.tearDown()
		checkForVerificationErrors()
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

		def match = name =~ /^(assert|verify|waitFor)(.+)$/
		if (match.find()) {
			def condition = match[0][1]
			def command = match[0][2]

			def result
			if (Selenium.metaClass.respondsTo(selenium, "is$command")) {
				handled = true
				switch (condition) {
					case "assert":
						SeleneseTestBase.assertTrue selenium."is$command"(* args)
						break
					case "verify":
						base.verifyTrue selenium."is$command"(* args)
						break
					case "waitFor":
						waitFor {
							selenium."is$command"(* args)
						}
						break
				}
			} else if (Selenium.metaClass.respondsTo(selenium, "get$command")) {
				handled = true
				use(ArrayCategory) {
					def expected = args.head()
					def seleniumArgs = args.tail()
					switch (condition) {
						case "assert":
							SeleneseTestBase.assertEquals expected, selenium."get$command"(* seleniumArgs)
							break
						case "verify":
							base.verifyEquals expected, selenium."get$command"(* seleniumArgs)
							break
						case "waitFor":
							waitFor {
								expected == selenium."get$command"(* seleniumArgs)
							}
							break
					}
				}
			}
		}

		if (!handled) throw new MissingMethodException(name, getClass(), args)
	}
}
