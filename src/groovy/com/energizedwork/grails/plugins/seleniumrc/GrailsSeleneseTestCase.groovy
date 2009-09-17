package com.energizedwork.grails.plugins.seleniumrc

import com.thoughtworks.selenium.GroovySelenium
import com.thoughtworks.selenium.SeleneseTestBase
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * An adaptation of GroovySeleneseTestCase that expects a Selenium instance to be injected
 * by the test runner rather than creating its own. This means an entire suite of tests can
 * be run in a single browser session.
 */
class GrailsSeleneseTestCase extends GroovyTestCase {
    static final BASE_METHODS = SeleneseTestBase.class.methods

    private SeleneseTestBase base
    private int defaultTimeout
	GroovySelenium selenium

    GrailsSeleneseTestCase() {
        super()
        base = new SeleneseTestBase()
    }

    @Override
    void setUp() {
		super.setUp()
		setTestContext()
		defaultTimeout = SeleniumManager.instance.config.selenium.defaultTimeout
		switch (SeleniumManager.instance.config.selenium.screenshots) {
			case "always":
				alwaysCaptureScreenshots = true
				captureScreenshotOnFailure = false
				break
			case "onfailure":
				alwaysCaptureScreenshots = false
				captureScreenshotOnFailure = true
				break
			default:
				alwaysCaptureScreenshots = false
				captureScreenshotOnFailure = false
		}
		selenium.screenshotDir = new File(SeleniumManager.instance.config.selenium.screenshotDir)
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
     * @param timeout    maximum time to wait for condition to be satisfied, in
     *                   milliseconds. If unspecified, the default timeout is
     *                   used; the default value can be set with
     *                   setDefaultTimeout().
     * @param condition  the condition to wait for. The Closure should return
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
        def method = BASE_METHODS.find { it.getName() == name }
        if (method) {
            return method.invoke(base, args)
        }

        throw new MissingMethodException(name, getClass(), args)
    }
}