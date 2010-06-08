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

class SeleniumRcGrailsPlugin {

	def version = "1.0.2"
	def grailsVersion = "1.2.0 > *"
	def dependsOn = [:]
	def pluginExcludes = [
			"grails-app/views/**",
			"web-app/**"
	]

	// plugin should not be bundled in application war
	def scopes = [excludes: "war"]

	def author = "Rob Fletcher"
	def authorEmail = "rob@energizedwork.com"
	def title = "Selenium RC Plugin"
	def description = '''\\
Runs Selenium RC tests written in Groovy in the Grails functional test phase.
'''
	def documentation = "http://robfletcher.github.com/grails-selenium-rc"

	def doWithWebDescriptor = {xml ->
	}

	def doWithSpring = {
	}

	def doWithDynamicMethods = {ctx ->
	}

	def doWithApplicationContext = {applicationContext ->
	}

	def onChange = {event ->
	}

	def onConfigChange = {event ->
	}
}
