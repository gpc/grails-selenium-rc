package com.energizedwork.grails.plugins.seleniumrc

import junit.framework.TestSuite
import org.codehaus.groovy.grails.test.DefaultGrailsTestHelper
import grails.util.BuildSettings

class GrailsSeleniumTestHelper extends DefaultGrailsTestHelper {

	GrailsSeleniumTestHelper(BuildSettings settings, ClassLoader parentLoader, Closure resourceResolver) {
		super(settings, parentLoader, resourceResolver)
	}

	TestSuite createTestSuite(Class clazz) {
		return new SeleniumTestSuite(clazz)
	}

	TestSuite createTestSuite() {
		return new SeleniumTestSuite()
	}

}