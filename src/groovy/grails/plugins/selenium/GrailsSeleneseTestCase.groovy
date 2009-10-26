package grails.plugins.selenium

import com.thoughtworks.selenium.SeleneseTestBase
import com.thoughtworks.selenium.Selenium

/**
 * An adaptation of GroovySeleneseTestCase that expects a Selenium instance to be injected
 * by the test runner rather than creating its own. This means an entire suite of tests can
 * be run in a single browser session.
 */
@Mixin(SeleneseTestBase)
@Mixin(Selenese)
class GrailsSeleneseTestCase extends GroovyTestCase {

	private int defaultTimeout

	@Override
	void setUp() {
		super.setUp()
		setTestContext()
		defaultTimeout = config.selenium.defaultTimeout

		// need to do this in every setUp/tearDown cycle or we will attempt
		// to grab screenshots of the server stopping, etc.
//		selenium.alwaysCaptureScreenshots = config.selenium.alwaysCaptureScreenshots
//		selenium.captureScreenshotOnFailure = config.selenium.captureScreenshotOnFailure
	}

	@Override
	void tearDown() {
		super.tearDown()
		checkForVerificationErrors()
//		selenium.alwaysCaptureScreenshots = false
//		selenium.captureScreenshotOnFailure = false
	}

	void setDefaultTimeout(int timeout) {
		assert selenium != null

		defaultTimeout = timeout
		selenium.setDefaultTimeout(timeout)
	}

//	void setAlwaysCaptureScreenshots(boolean capture) {
//		selenium.setAlwaysCaptureScreenshots(capture)
//	}
//
//	void setCaptureScreenshotOnFailure(boolean capture) {
//		selenium.setCaptureScreenshotOnFailure(capture)
//	}

	void setTestContext() {
		selenium.setContext("${getClass().getSimpleName()}.${getName()}")
	}

	/**
	 * Delegates missing method calls to the SeleneseTestBase object where
	 * possible.
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
						verifyTrue selenium."is$command"(* args)
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
							verifyEquals expected, selenium."get$command"(* seleniumArgs)
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
