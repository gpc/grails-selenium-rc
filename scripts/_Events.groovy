import grails.util.GrailsUtil

includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumConfig.groovy")

eventCreateWarStart = { warName, stagingDir ->
	ant.delete dir: "${stagingDir}/WEB-INF/classes/grails/plugins/selenium"
	ant.delete dir: "${stagingDir}/plugins/selenium-rc-0.2"
}

loadPluginClass = { String className ->
	def doLoad = {-> classLoader.loadClass(className) }
	try {
		doLoad()
	} catch (ClassNotFoundException e) {
		event "StatusUpdate", ["Could not load $className, compiling"]
		includeTargets << grailsScript("_GrailsCompile")
		compile()
		doLoad()
	}
}

eventAllTestsStart = {
	loadSeleniumConfig()

	def phase = "functional"
	if (seleniumConfig.selenium.remote) {
		event "StatusUpdate", ["Running Selenium in remote mode"]
		phase = "other"
	}

	event "StatusUpdate", ["Selenium tests will run in the ${phase} phase"]

	def testTypeClass = getJUnitTestType()
	def testType = testTypeClass.newInstance("selenium", "selenium")
	def seleniumTestTypeClass = loadPluginClass("grails.plugins.selenium.test.support.SeleniumGrailsTestType")
	binding."${phase}Tests" << seleniumTestTypeClass.newInstance(testType, seleniumConfig)

	if (binding.variables.containsKey("spockPluginDir")) {
		def specTestTypeClass = loadSpecTestTypeClass()
		def spockTestType = specTestTypeClass.newInstance("spock-selenium", "selenium")
		binding."${phase}Tests" << seleniumTestTypeClass.newInstance(spockTestType, seleniumConfig)
	}
}

getJUnitTestType = { ->
	if (GrailsUtil.grailsVersion.startsWith("1.3")) {
		testType = classLoader.loadClass("org.codehaus.groovy.grails.test.junit4.JUnit4GrailsTestType")
	} else {
		testType = classLoader.loadClass("org.codehaus.groovy.grails.test.junit3.JUnit3GrailsTestType")
	}
}
