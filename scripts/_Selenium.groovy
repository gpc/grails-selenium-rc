/*
 * Copyright 2010 Rob Fletcher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumConfig.groovy")
includeTargets << new File("$seleniumRcPluginDir/scripts/_SeleniumServer.groovy")

seleniumRunner = null
selenium = null

target(registerSeleniumTestType: "Registers the selenium test type with the appropriate test phase") {
	depends(loadSeleniumConfig)

	if (testOptions.remote || seleniumConfig.selenium.remote) {
		event "StatusUpdate", ["Running Selenium in remote mode"]
		// override the functional test phase prep so it does not start the app
		// TODO: this is a little crude but we need some setup to be present but without starting the app
		functionalTestPhasePreparation = integrationTestPhasePreparation
		functionalTestPhaseCleanUp = integrationTestPhaseCleanUp
	}

	binding.functionalTests << "selenium"

	if (binding.variables.containsKey("spockPluginDir")) {
		def specTestTypeClass = loadSpecTestTypeClass()
		binding.functionalTests << specTestTypeClass.newInstance("spock-selenium", "selenium")
	}
}

target(startSelenium: "Starts Selenium and launches a browser") {
	startSeleniumServer()

	// this isn't done in initial config construction as it requires app config to be loaded and use of -clean can cause problems
	depends(determineSeleniumUrl)
	
	event "StatusUpdate", ["Starting Selenium session for $seleniumConfig.selenium.url"]
	seleniumRunner = Class.forName("grails.plugins.selenium.SeleniumRunner", true, classLoader).newInstance()
	selenium = seleniumRunner.startSelenium(seleniumConfig)
}

target(stopSelenium: "Stops Selenium") {
	event "StatusUpdate", ["Stopping Selenium session"]
	seleniumRunner.stopSelenium()
	selenium = null
	stopSeleniumServer()
}

target(registerSeleniumTestListeners: "Registers listeners for the Selenium test lifecycle") {
	eventListener.addGrailsBuildListener(Class.forName("grails.plugins.selenium.lifecycle.TestContextNotifier", true, classLoader).newInstance(selenium))
	eventListener.addGrailsBuildListener(Class.forName("grails.plugins.selenium.lifecycle.ScreenshotGrabber", true, classLoader).newInstance(selenium, seleniumConfig))
}

target(determineSeleniumUrl: "Determines URL Selenium tests will connect to") {
	if (!seleniumConfig.selenium.url) {
		depends(configureServerContextPath, createConfig)
		def url
		if (config.grails.serverURL) {
			url = config.grails.serverURL
		} else {
			def host = serverHost ?: "localhost"
			def port = serverPort
			def path = serverContextPath
			url = "http://$host:${port}$path"
		}
		if (!url.endsWith("/")) url = "$url/"
		seleniumConfig.selenium.url = url
	}
}

