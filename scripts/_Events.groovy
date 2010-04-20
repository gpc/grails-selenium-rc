includeTargets << new File("$seleniumRcPluginDir/scripts/_Selenium.groovy")
includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumConfig.groovy")
includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumServer.groovy")

eventCreateWarStart = { warName, stagingDir ->
	ant.delete dir: "${stagingDir}/WEB-INF/classes/grails/plugins/selenium"
	ant.delete dir: "${stagingDir}/plugins/selenium-rc-0.2"
}

eventAllTestsStart = {
	registerSeleniumTestType()
}

eventTestSuiteStart = {String type ->
	if (type =~ /selenium/) {
		startSelenium()
		eventListener.addGrailsBuildListener("grails.plugins.selenium.lifecycle.TestContextNotifier")
		eventListener.addGrailsBuildListener("grails.plugins.selenium.lifecycle.ScreenshotGrabber")
	}
}

eventTestSuiteEnd = {String type ->
	if (type =~ /selenium/) {
		stopSelenium()
	}
}
