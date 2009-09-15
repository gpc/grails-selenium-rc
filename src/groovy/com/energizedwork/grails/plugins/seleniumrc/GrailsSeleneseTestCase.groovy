package com.energizedwork.grails.plugins.seleniumrc

import com.thoughtworks.selenium.GroovySelenium
import com.thoughtworks.selenium.SeleneseTestBase
import org.apache.commons.lang.StringUtils
import com.thoughtworks.selenium.Selenium

/**
 * The Groovy equivalent of SeleneseTestCase, as a GroovyTestCase.
 */
class GrailsSeleneseTestCase extends GroovyTestCase {
	public static final BASE_METHODS = SeleneseTestBase.class.methods

	private SeleneseTestBase base
	private int defaultTimeout

	GrailsSeleneseTestCase() {
		super()
		base = new SeleneseTestBase()
		defaultTimeout = 60000
	}

	@Override
	void setUp() {
		super.setUp()
		setTestContext()
	}

	@Override
	void tearDown() {
		super.tearDown()
		base.checkForVerificationErrors()
	}

	/**
	 * Returns the delegate for most Selenium API calls.
	 */
	SeleneseTestBase getBase() {
		return base
	}

	GroovySelenium getSelenium() {
		return SeleniumManager.instance.selenium
	}

	void setDefaultTimeout(int timeout) {
		assert selenium != null

		defaultTimeout = timeout
		selenium.setDefaultTimeout(timeout)
	}

	void setAlwaysCaptureScreenshots(boolean capture) {
		selenium.setAlwaysCaptureScreenshots(capture)
	}

	void setCaptureScreenshotOnFailure(boolean capture) {
		selenium.setCaptureScreenshotOnFailure(capture)
	}

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
		switch (name) {
			case ~/^assert\w+/:
				def condition = StringUtils.substringAfter(name, "assert")
				if (Selenium.metaClass.respondsTo(selenium, "is$condition")) {
					handled = true
					boolean result = selenium."is$condition"(* args)
					base.assertTrue(result)
				} else if (Selenium.metaClass.respondsTo(selenium, "get$condition")) {
					handled = true
					def expected = args[-1]
					def getArgs = args.size() > 1 ? args[0..-2] : [] as Object[]
					def result = selenium."get$condition"(* getArgs)
					base.assertEquals(expected, result)
				}
				break
			case ~/^verify\w+/:
				def condition = StringUtils.substringAfter(name, "verify")
				if (Selenium.metaClass.respondsTo(selenium, "is$condition")) {
					handled = true
					boolean result = selenium."is$condition"(* args)
					base.verifyTrue(result)
				} else if (Selenium.metaClass.respondsTo(selenium, "get$condition")) {
					handled = true
					def expected = args[-1]
					def getArgs = args.size() > 1 ? args[0..-2] : [] as Object[]
					def result = selenium."get$condition"(* getArgs)
					base.verifyEquals(expected, result)
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
					def expected = args[-1]
					def getArgs = args.size() > 1 ? args[0..-2] : [] as Object[]
					waitFor {
						selenium."get$condition"(* getArgs)
					}
				}
				break
		}
		if (!handled) throw new MissingMethodException(name, getClass(), args)
	}
}