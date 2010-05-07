class SeleniumRcGrailsPlugin {

	def version = "1.0.2-SNAPSHOT"
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
