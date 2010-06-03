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

includeTargets << new File("$seleniumRcPluginDir/scripts/_Selenium.groovy")

eventCreateWarStart = { warName, stagingDir ->
	ant.delete dir: "${stagingDir}/WEB-INF/classes/grails/plugins/selenium"
	ant.delete(includeEmptyDirs: true) {
		fileset dir: "${stagingDir}/plugins", includes: "selenium-rc-**/*"	
	}
}

eventAllTestsStart = {
	registerSeleniumTestType()
}

eventTestSuiteStart = {String type ->
	if (type =~ /selenium/) {
		startSelenium()
		registerSeleniumTestListeners()
	}
}

eventTestSuiteEnd = {String type ->
	if (type =~ /selenium/) {
		stopSelenium()
	}
}
