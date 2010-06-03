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

import grails.util.GrailsUtil

seleniumUrl = null
seleniumConfig = null

target(loadSeleniumConfig: "Loads Selenium config into seleniumConfig variable") {
	event "StatusUpdate", ["loading selenium config"]
	depends(loadDefaultConfig, mergeApplicationConfig, mergeSystemProperties)
}

target(loadDefaultConfig: "Loads default Selenium configuration") {
	def defaultConfig = """
selenium {
	server {
		host = "localhost"
		port = 4444
	}
	browser = "${getDefaultBrowser()}"
	defaultTimeout = 60000
	slow = false
	singleWindow = true
	windowMaximize = false
	screenshot {
		dir = "${testReportsDir.path.replace(File.separator, '/')}/screenshots"
		onFail = false
	}
}
		"""
	seleniumConfig = new ConfigSlurper(GrailsUtil.environment).parse(defaultConfig)
}

target(mergeApplicationConfig: "Loads Selenium config from grails-app/conf/SeleniumConfig.groovy") {
	def configClass = getSeleniumConfigClass()
	if (configClass) {
		def slurper = new ConfigSlurper(GrailsUtil.environment)
		seleniumConfig.merge slurper.parse(configClass)
	}
}

target(mergeSystemProperties: "Loads Selenium config overrides from system properties") {
	// override settings with system properties
	def sysProps = new StringBuilder()
	System.properties.each {
		if (it.key =~ /^selenium\./) {
			sysProps << it.key << "="
			if (it.value in ["true", "false"] || it.value.isInteger()) sysProps << it.value
			else sysProps << "'" << it.value << "'"
			sysProps << "\n"
		}
	}
	if (sysProps) {
		def slurper = new ConfigSlurper(GrailsUtil.environment)
		seleniumConfig.merge slurper.parse(sysProps.toString())
	}
}

private URL getSeleniumConfigClass() {
	def configFile = new File(basedir, "grails-app/conf/SeleniumConfig.groovy")
	if (configFile.isFile()) {
		return configFile.toURI().toURL()
	} else {
		event "StatusUpdate", ["SeleniumConfig.groovy not found, proceeding without config file"]
		return null
	}
}

private getDefaultBrowser() {
	switch (System.properties."os.name") {
		case ~/^Mac OS.*/:
			return "*safari"
		case ~/^Windows.*/:
			return "*iexplore"
		default:
			return "*firefox"
	}
}

