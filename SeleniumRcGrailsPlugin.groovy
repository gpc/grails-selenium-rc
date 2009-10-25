class SeleniumRcGrailsPlugin {
	// the plugin version
	def version = "0.1"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "1.1 > *"
	// the other plugins this plugin depends on
	def dependsOn = [:]
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
			"grails-app/views/error.gsp",
			"lib/gmock-0.8.0.jar" // TODO: this doesn't work
	]
	def environments = ["test"]

	// TODO Fill in these fields
	def author = "Rob Fletcher"
	def authorEmail = "rob@energizedwork.com"
	def title = "Selenium RC Plugin"
	def description = '''\\
Runs Selenium RC tests written in Groovy in the Grails functional test phase.
'''

	// URL to the plugin's documentation
	def documentation = "http://grails.org/SeleniumRc+Plugin"

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
