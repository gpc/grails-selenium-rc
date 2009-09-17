package com.energizedwork.grails.plugins.seleniumrc

import com.energizedwork.grails.plugins.seleniumrc.GrailsSeleneseTestCase
import com.energizedwork.grails.plugins.seleniumrc.SeleniumManager
import junit.framework.Test
import junit.framework.TestResult
import junit.framework.TestSuite

class SeleniumTestSuite extends TestSuite {

	SeleniumTestSuite() {
		super()
	}

	public SeleniumTestSuite(Class clazz) {
		super(clazz)
	}

	void runTest(Test test, TestResult result) {
		if (test instanceof GrailsSeleneseTestCase) {
			test.selenium = SeleniumManager.instance.selenium
		}
		super.runTest(test, result)
	}

}