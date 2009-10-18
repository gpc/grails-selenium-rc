package grails.plugins.selenium

import com.thoughtworks.selenium.GroovySelenium
import com.thoughtworks.selenium.SeleneseTestBase
import org.apache.commons.lang.StringUtils
import com.thoughtworks.selenium.Selenium
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * An adaptation of GroovySeleneseTestCase that expects a Selenium instance to be injected
 * by the test runner rather than creating its own. This means an entire suite of tests can
 * be run in a single browser session.
 */
@Mixin(SeleneseTestBase)
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

	ConfigObject getConfig() {
		SeleniumManager.instance.config
	}

	GrailsSelenium getSelenium() {
		SeleniumManager.instance.selenium
	}

	/**
	 * Returns the URL context path for the application.
	 */
	String getRootURL() {
		return "/${ConfigurationHolder.config."web.app.context.path" ?: ApplicationHolder.application.metadata."app.name"}"
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
	 * Convenience method for conditional waiting. Returns when the condition
	 * is satisfied, or fails the test if the timeout is reached.
	 *
	 * @param timeout maximum time to wait for condition to be satisfied, in
	 *                   milliseconds. If unspecified, the default timeout is
	 *                   used; the default value can be set with
	 *                   setDefaultTimeout().
	 * @param condition the condition to wait for. The Closure should return
	 *                   true when the condition is satisfied.
	 */
	void waitFor(int timeout = defaultTimeout, Closure condition) {
		assert timeout > 0

		def timeoutTime = System.currentTimeMillis() + timeout
		while (System.currentTimeMillis() < timeoutTime) {
			try {
				if (condition.call()) {
					return
				}
			}
			catch (e) {}
			sleep(500)
		}

		fail('timeout')
	}

	/**
	 * Delegates missing method calls to the SeleneseTestBase object where
	 * possible.
	 */
	def methodMissing(String name, args) {
		boolean handled = false
		// TODO: 3 branches doing almost the same thing - could benefit from refactoring
		switch (name) {
			case ~/^assert\w+/:
				def condition = StringUtils.substringAfter(name, "assert")
				if (Selenium.metaClass.respondsTo(selenium, "is$condition")) {
					handled = true
					boolean result = selenium."is$condition"(* args)
					SeleneseTestBase.assertTrue(result)
				} else if (Selenium.metaClass.respondsTo(selenium, "get$condition")) {
					handled = true
					use(ArrayCategory) {
						def expected = args.head()
						def getArgs = args.tail()
						def result = selenium."get$condition"(* getArgs)
						SeleneseTestBase.assertEquals(expected, result)
					}
				}
				break
			case ~/^verify\w+/:
				def condition = StringUtils.substringAfter(name, "verify")
				if (Selenium.metaClass.respondsTo(selenium, "is$condition")) {
					handled = true
					boolean result = selenium."is$condition"(* args)
					verifyTrue(result)
				} else if (Selenium.metaClass.respondsTo(selenium, "get$condition")) {
					handled = true
					use(ArrayCategory) {
						def expected = args.head()
						def getArgs = args.tail()
						def result = selenium."get$condition"(* getArgs)
						verifyEquals(expected, result)
					}
				}
				break
			case ~/^waitFor\w+/:
				def condition = StringUtils.substringAfter(name, "waitFor")
				if (Selenium.metaClass.respondsTo(selenium, "is$condition")) {
					handled = true
					waitFor {
						selenium."is$condition"(* args)
					}
				} else if (Selenium.metaClass.respondsTo(selenium, "get$condition")) {
					handled = true
					use(ArrayCategory) {
						def expected = args.head()
						def getArgs = args.tail()
						waitFor {
							selenium."get$condition"(* getArgs) == expected
						}
					}
				}
				break
		}
		if (!handled) throw new MissingMethodException(name, getClass(), args)
	}
}

@Category(Object[]) class ArrayCategory {
	Object head() {
		length > 0 ? this[0] : null
	}

	Object[] tail() {
		length > 1 ? this[1..length - 1] : [] as Object[]
	}
}